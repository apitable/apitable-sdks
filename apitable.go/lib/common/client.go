package common

import (
	"bytes"
	"encoding/json"
	"fmt"
	aterror "github.com/apitable/apitable-sdks/apitable.go/lib/common/error"
	athttp "github.com/apitable/apitable-sdks/apitable.go/lib/common/http"
	"github.com/apitable/apitable-sdks/apitable.go/lib/common/profile"
	"io"
	"log"
	"mime/multipart"
	"net/http"
	"net/http/httputil"
	"net/textproto"
	"os"
	"path"
	"strings"
	"time"
)

type Client struct {
	httpClient  *http.Client
	httpProfile *profile.HttpProfile
	profile     *profile.ClientProfile
	credential  *Credential
	debug       bool
}

func (c *Client) Init() *Client {
	c.httpClient = &http.Client{}
	c.debug = false
	log.SetFlags(log.LstdFlags | log.Lshortfile)
	return c
}

func (c *Client) WithCredential(cred *Credential) *Client {
	c.credential = cred
	return c
}

func (c *Client) WithProfile(clientProfile *profile.ClientProfile) *Client {
	c.profile = clientProfile
	c.httpProfile = clientProfile.HttpProfile
	c.httpClient.Timeout = time.Duration(c.httpProfile.ReqTimeout) * time.Second
	c.debug = clientProfile.Debug
	return c
}

func (c *Client) WithDebug(flag bool) *Client {
	c.debug = flag
	return c
}

func FileBuffer(filePath string) ([]byte, string, error) {
	// create a new buffer, to storage file content.
	bodyBuffer := &bytes.Buffer{}
	// create a multipart file writer, to conveniently write content according to http format.
	bodyWriter := multipart.NewWriter(bodyBuffer)
	file, err := os.Open(filePath)
	if err != nil {
		msg := fmt.Sprintf("Fail to get response because %s", err)
		return nil, "", aterror.NewSDKError(500, msg, "ClientError.FileReadError")
	}
	mimeType, err := GetFileContentType(file)
	if err != nil {
		msg := fmt.Sprintf("Fail to get response because %s", err)
		return nil, "", aterror.NewSDKError(500, msg, "ClientError.FileReadError")
	}
	fileWriter, err := createFormFile(bodyWriter, "file", filePath, mimeType)
	if err != nil {
		msg := fmt.Sprintf("Fail to get response because %s", err)
		return nil, "", aterror.NewSDKError(500, msg, "ClientError.MultipartError")
	}
	// don't forget closing opening file.
	defer file.Close()
	_, err = io.Copy(fileWriter, file)
	if err != nil {
		return nil, "", nil
	}
	// close bodyWriter to stop writing content.
	_ = bodyWriter.Close()
	contentType := bodyWriter.FormDataContentType()
	return bodyBuffer.Bytes(), contentType, nil
}

func (c *Client) Send(request athttp.Request, response athttp.Response) (err error) {
	if request.GetScheme() == "" {
		request.SetScheme(c.httpProfile.Scheme)
	}

	if request.GetDomain() == "" {
		domain := c.httpProfile.Domain
		if domain == "" {
			domain = athttp.Domain
		}
		request.SetDomain(domain)
	}

	if request.GetHttpMethod() == "" {
		request.SetHttpMethod(c.httpProfile.ReqMethod)
	}
	return c.sendWithToken(request, response)
}

func (c *Client) sendWithToken(request athttp.Request, response athttp.Response) (err error) {
	headers := map[string]string{
		"User-Agent": "lib-go",
	}
	if c.credential.Token != "" {
		headers["Authorization"] = "Bearer " + c.credential.Token
	}
	headers["Content-Type"] = request.GetContentType()

	// start process

	// build canonical request string
	httpRequestMethod := request.GetHttpMethod()
	canonicalQueryString := ""
	if httpRequestMethod == athttp.GET || httpRequestMethod == athttp.DELETE {
		err = athttp.ConstructParams(request)
		if err != nil {
			return err
		}
		params := make(map[string]string)
		for key, value := range request.GetParams() {
			params[key] = value
		}
		canonicalQueryString = athttp.GetUrlQueriesEncoded(params)
	}
	requestPayload := ""
	if httpRequestMethod == athttp.POST || httpRequestMethod == athttp.PATCH {
		if c.profile.Upload {
			requestPayload = string(request.GetFile())
		} else {
			b, err := json.Marshal(request)
			if err != nil {
				return err
			}
			requestPayload = string(b)
		}
	}

	url := request.GetScheme() + "://" + request.GetDomain() + request.GetPath()
	if canonicalQueryString != "" {
		url = url + "?" + canonicalQueryString
	}
	httpRequest, err := http.NewRequest(httpRequestMethod, url, strings.NewReader(requestPayload))
	if err != nil {
		return err
	}
	for k, v := range headers {
		httpRequest.Header[k] = []string{v}
	}
	if c.debug {
		outbytes, err := httputil.DumpRequest(httpRequest, true)
		if err != nil {
			log.Printf("[ERROR] dump request failed because %s", err)
			return err
		}
		log.Printf("[DEBUG] http request = %s", outbytes)
	}
	httpResponse, err := c.httpClient.Do(httpRequest)
	if err != nil {
		msg := fmt.Sprintf("Fail to get response because %s", err)
		return aterror.NewSDKError(500, msg, "ClientError.NetworkError")
	}
	err = athttp.ParseFromHttpResponse(httpResponse, response)
	return err
}

// rewrite file type
func createFormFile(bodyWriter *multipart.Writer, fieldname, filePath string, contentType string) (io.Writer, error) {
	h := make(textproto.MIMEHeader)
	h.Set("Content-Disposition",
		fmt.Sprintf(`form-data; name="%s"; filename="%s"`,
			fieldname, path.Base(filePath)))
	h.Set("Content-Type", contentType)
	return bodyWriter.CreatePart(h)
}

func GetFileContentType(out *os.File) (string, error) {
	// Only the first 512 bytes are used to sniff the content type.
	buffer := make([]byte, 512)
	_, err := out.Read(buffer)
	if err != nil {
		return "", err
	}
	// Use the net/http package's handy DectectContentType function. Always returns a valid
	// content-type by returning "application/octet-stream" if no others seemed to match.
	contentType := http.DetectContentType(buffer)
	return contentType, nil
}
