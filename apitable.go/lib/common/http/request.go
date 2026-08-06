package http

import (
	"io"
	"net/url"
	"reflect"
	"strconv"
	"strings"
)

const (
	POST   = "POST"
	GET    = "GET"
	PATCH  = "PATCH"
	DELETE = "DELETE"

	HTTP  = "http"
	HTTPS = "https"

	Domain = "aitable.ai"
	Path   = "/"
)

const (
	FormContent = "application/x-www-form-urlencoded"
	JsonContent = "application/json"
)

type Request interface {
	GetBodyReader() io.Reader
	GetScheme() string
	GetDomain() string
	GetHttpMethod() string
	GetParams() map[string]string
	GetPath() string
	GetUrl() string
	GetContentType() string
	GetFile() []byte
	SetScheme(string)
	SetDomain(string)
	SetHttpMethod(string)
	SetPath(string)
	SetContentType(string)
	SetFile([]byte)
}

type BaseRequest struct {
	httpMethod  string
	scheme      string
	domain      string
	path        string
	contentType string
	file        []byte
	params      map[string]string
	formParams  map[string]string
}

func (r *BaseRequest) GetHttpMethod() string {
	return r.httpMethod
}

func (r *BaseRequest) GetParams() map[string]string {
	return r.params
}

func (r *BaseRequest) GetPath() string {
	return r.path
}

func (r *BaseRequest) GetDomain() string {
	return r.domain
}

func (r *BaseRequest) GetScheme() string {
	return r.scheme
}

func (r *BaseRequest) GetContentType() string {
	return r.contentType
}

func (r *BaseRequest) GetFile() []byte {
	return r.file
}

func (r *BaseRequest) SetDomain(domain string) {
	r.domain = domain
}

func (r *BaseRequest) SetFile(byte []byte) {
	r.file = byte
}

func (r *BaseRequest) SetContentType(contentType string) {
	r.contentType = contentType
}

func (r *BaseRequest) SetScheme(scheme string) {
	scheme = strings.ToLower(scheme)
	switch scheme {
	case HTTP:
		r.scheme = HTTP
	default:
		r.scheme = HTTPS
	}
}

func (r *BaseRequest) SetHttpMethod(method string) {
	switch strings.ToUpper(method) {
	case POST:
		{
			r.httpMethod = POST
		}
	case GET:
		{
			r.httpMethod = GET
		}
	case PATCH:
		{
			r.httpMethod = PATCH
		}
	case DELETE:
		{
			r.httpMethod = DELETE
		}
	default:
		{
			r.httpMethod = GET
		}
	}
}

func (r *BaseRequest) SetPath(path string) {
	r.path = path
}

func (r *BaseRequest) GetUrl() string {
	if r.httpMethod == GET {
		return r.GetScheme() + "://" + r.domain + r.path + "?" + GetUrlQueriesEncoded(r.params)
	} else if r.httpMethod == POST {
		return r.GetScheme() + "://" + r.domain + r.path
	} else {
		return ""
	}
}

func GetUrlQueriesEncoded(params map[string]string) string {
	values := url.Values{}
	for key, value := range params {
		if value != "" {
			values.Add(key, value)
		}
	}
	return values.Encode()
}

func (r *BaseRequest) GetBodyReader() io.Reader {
	if r.httpMethod == POST {
		s := GetUrlQueriesEncoded(r.params)
		return strings.NewReader(s)
	} else {
		return strings.NewReader("")
	}
}

func (r *BaseRequest) Init() *BaseRequest {
	r.path = Path
	r.params = make(map[string]string)
	r.formParams = make(map[string]string)
	r.contentType = FormContent
	return r
}

func ConstructParams(req Request) (err error) {
	value := reflect.ValueOf(req).Elem()
	err = flatStructure(value, req, "")
	//log.Printf("[DEBUG] params=%s", req.GetParams())
	return
}

func flatStructure(value reflect.Value, request Request, prefix string) (err error) {
	//log.Printf("[DEBUG] reflect value: %v", value.Type())
	valueType := value.Type()
	for i := 0; i < valueType.NumField(); i++ {
		tag := valueType.Field(i).Tag
		nameTag, hasNameTag := tag.Lookup("name")
		if !hasNameTag {
			continue
		}
		field := value.Field(i)
		kind := field.Kind()
		if kind == reflect.Ptr && field.IsNil() {
			continue
		}
		if kind == reflect.Ptr {
			field = field.Elem()
			kind = field.Kind()
		}
		key := prefix + nameTag
		if kind == reflect.String {
			s := field.String()
			if s != "" {
				request.GetParams()[key] = s
			}
		} else if kind == reflect.Bool {
			request.GetParams()[key] = strconv.FormatBool(field.Bool())
		} else if kind == reflect.Int || kind == reflect.Int64 {
			request.GetParams()[key] = strconv.FormatInt(field.Int(), 10)
		} else if kind == reflect.Uint || kind == reflect.Uint64 {
			request.GetParams()[key] = strconv.FormatUint(field.Uint(), 10)
		} else if kind == reflect.Float64 {
			request.GetParams()[key] = strconv.FormatFloat(field.Float(), 'f', -1, 64)
		} else if kind == reflect.Slice {
			list := value.Field(i)
			for j := 0; j < list.Len(); j++ {
				vj := list.Index(j)
				key := prefix + nameTag + "." + strconv.Itoa(j)
				kind = vj.Kind()
				if kind == reflect.Ptr && vj.IsNil() {
					continue
				}
				if kind == reflect.Ptr {
					vj = vj.Elem()
					kind = vj.Kind()
				}
				if kind == reflect.String {
					request.GetParams()[key] = vj.String()
				} else if kind == reflect.Bool {
					request.GetParams()[key] = strconv.FormatBool(vj.Bool())
				} else if kind == reflect.Int || kind == reflect.Int64 {
					request.GetParams()[key] = strconv.FormatInt(vj.Int(), 10)
				} else if kind == reflect.Uint || kind == reflect.Uint64 {
					request.GetParams()[key] = strconv.FormatUint(vj.Uint(), 10)
				} else if kind == reflect.Float64 {
					request.GetParams()[key] = strconv.FormatFloat(vj.Float(), 'f', -1, 64)
				} else {
					if err = flatStructure(vj, request, key+"."); err != nil {
						return
					}
				}
			}
		} else {
			if err = flatStructure(reflect.ValueOf(field.Interface()), request, prefix+nameTag+"."); err != nil {
				return
			}
		}
	}
	return
}
