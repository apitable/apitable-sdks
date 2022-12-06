// Package error provides custom sdk error
package error

import "fmt"

type SDKError struct {
	Code      int
	Message   string
	RequestId string
}

func (e *SDKError) Error() string {
	return fmt.Sprintf("[SDKError] Code=%d, Message=%s, RequestId=%s", e.Code, e.Message, e.RequestId)
}

func NewSDKError(code int, message, requestId string) error {
	return &SDKError{
		Code:      code,
		Message:   message,
		RequestId: requestId,
	}
}

func (e *SDKError) GetCode() int {
	return e.Code
}

func (e *SDKError) GetMessage() string {
	return e.Message
}

func (e *SDKError) GetRequestId() string {
	return e.RequestId
}
