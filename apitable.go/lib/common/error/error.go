// Package error provides custom sdk error
package error

import "fmt"

type APITableError struct {
	Code      int
	Message   string
	RequestId string
}

func (e *APITableError) Error() string {
	return fmt.Sprintf("[SDKError] Code=%d, Message=%s, RequestId=%s", e.Code, e.Message, e.RequestId)
}

func NewAPITableSDKError(code int, message, requestId string) error {
	return &APITableError{
		Code:      code,
		Message:   message,
		RequestId: requestId,
	}
}

func (e *APITableError) GetCode() int {
	return e.Code
}

func (e *APITableError) GetMessage() string {
	return e.Message
}

func (e *APITableError) GetRequestId() string {
	return e.RequestId
}
