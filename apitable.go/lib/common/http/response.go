// Package http provides basic http request/response method and definition
package http

import (
	"encoding/json"
	"fmt"
	aterror "github.com/apitable/apitable-sdks/apitable.go/lib/common/error"
	"io/ioutil"
	//"log"
	"net/http"
)

type Response interface {
	ParseErrorFromHTTPResponse(body []byte) error
}

type BaseResponse struct {
	Code    int    `json:"code"`
	Message string `json:"message"`
	Success bool   `json:"success"`
	// unique request id, will be return each time request.
	// when positioning question, it's necessary to provider request id.
	RequestId *string
}

type ErrorResponse struct {
	BaseResponse
}

func (r *BaseResponse) ParseErrorFromHTTPResponse(body []byte) (err error) {
	resp := &ErrorResponse{}
	err = json.Unmarshal(body, resp)
	if err != nil {
		msg := fmt.Sprintf("Fail to parse json content: %s, because: %s", body, err)
		return aterror.NewAPITableSDKError(500, msg, "ClientError.ParseJsonError")
	}
	if resp.Code != 200 {
		return aterror.NewAPITableSDKError(resp.Code, resp.Message, resp.Message)
	}
	return nil
}

func ParseFromHttpResponse(hr *http.Response, response Response) (err error) {
	defer hr.Body.Close()
	body, err := ioutil.ReadAll(hr.Body)
	if err != nil {
		msg := fmt.Sprintf("Fail to read response body because %s", err)
		return aterror.NewAPITableSDKError(500, msg, "ClientError.IOError")
	}
	if !(hr.StatusCode == 200 || hr.StatusCode == 201) {
		msg := fmt.Sprintf("Request fail with http status code: %s, with body: %s", hr.Status, body)
		return aterror.NewAPITableSDKError(hr.StatusCode, msg, "ClientError.HttpStatusCodeError")
	}
	//log.Printf("[DEBUG] Response Body=%s", body)
	err = response.ParseErrorFromHTTPResponse(body)
	if err != nil {
		return
	}
	err = json.Unmarshal(body, &response)
	if err != nil {
		msg := fmt.Sprintf("Fail to parse json content: %s, because: %s", body, err)
		return aterror.NewAPITableSDKError(500, msg, "ClientError.ParseJsonError")
	}
	return
}
