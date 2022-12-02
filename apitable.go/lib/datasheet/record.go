// Package datasheet provides the operations about datasheet
package datasheet

import (
	"fmt"
	"github.com/apitable/apitable-sdks/apitable.go/lib/common"
	athttp "github.com/apitable/apitable-sdks/apitable.go/lib/common/http"
	"github.com/apitable/apitable-sdks/apitable.go/lib/common/profile"
	"math"
)

const maxPageSize = 1000
const recordPath = "/fusion/v1/datasheets/%s/records"
const attachPath = "/fusion/v1/datasheets/%s/attachments"

type Datasheet struct {
	common.Client
	DatasheetId string
}

// NewDatasheet init datasheet instance
func NewDatasheet(credential *common.Credential, datasheetId string, clientProfile *profile.ClientProfile) (datasheet *Datasheet, err error) {
	datasheet = &Datasheet{}
	datasheet.DatasheetId = datasheetId
	datasheet.Init().WithCredential(credential).WithProfile(clientProfile)
	return
}

// NewDescribeRecordRequest init datasheet record request instance
func NewDescribeRecordRequest() (request *DescribeRecordRequest) {
	request = &DescribeRecordRequest{
		BaseRequest: &athttp.BaseRequest{},
	}

	return
}

func NewCreateRecordsRequest() (request *CreateRecordsRequest) {
	request = &CreateRecordsRequest{
		BaseRequest: &athttp.BaseRequest{},
	}
	return
}

func NewModifyRecordsRequest() (request *ModifyRecordsRequest) {
	request = &ModifyRecordsRequest{
		BaseRequest: &athttp.BaseRequest{},
	}
	return
}

func NewDeleteRecordsRequest() (request *DeleteRecordsRequest) {
	request = &DeleteRecordsRequest{
		BaseRequest: &athttp.BaseRequest{},
	}
	return
}

func NewUploadRequest() (request *UploadRequest) {
	request = &UploadRequest{
		BaseRequest: &athttp.BaseRequest{},
	}
	return
}

func NewDescribeRecordResponse() (response *DescribeRecordResponse) {
	response = &DescribeRecordResponse{
		BaseResponse: &athttp.BaseResponse{},
	}
	return
}

func NewUploadResponse() (response *UploadResponse) {
	response = &UploadResponse{
		BaseResponse: &athttp.BaseResponse{},
	}
	return
}

// DescribeAllRecords use to query the details of all records.
//
// * according to `ViewId`, column name, `FieldId` or other information to query the record detailed information.
// * For details of filtering information please see `RecordRequest`。
// * If the parameter is empty, all records in the current datasheet are returned.
func (c *Datasheet) DescribeAllRecords(request *DescribeRecordRequest) (records []*Record, err error) {
	if request == nil {
		request = NewDescribeRecordRequest()
	}
	request.Init().SetPath(fmt.Sprintf(recordPath, c.DatasheetId))
	request.SetHttpMethod(athttp.GET)
	request.PageSize = common.Int64Ptr(maxPageSize)
	request.PageNum = common.Int64Ptr(1)
	response := NewDescribeRecordResponse()
	err = c.Send(request, response)
	if err != nil {
		return nil, err
	}
	total := response.Data.Total
	// calculate the total number of cycles.
	if *total > maxPageSize {
		times := int(math.Ceil(float64(*total / maxPageSize)))
		for i := 1; i <= times; i++ {
			request.PageNum = common.Int64Ptr(int64(i + 1))
			tmp := NewDescribeRecordResponse()
			err = c.Send(request, tmp)
			if err != nil {
				// if one error, all error.
				return nil, err
			}
			response.Data.Records = append(response.Data.Records, tmp.Data.Records...)
		}
	}
	response.Data.PageNum = common.Int64Ptr(0)
	return response.Data.Records, nil
}

// DescribeRecords use to query paging records' details.
//
// * according to `ViewId`, column name, `FieldId` or other information to query the record detailed information.
// * For details of filtering information please see `RecordRequest`。
// * If the parameter is empty, return paging according to the default. The default is 100 records per page.
func (c *Datasheet) DescribeRecords(request *DescribeRecordRequest) (pagination *RecordPagination, err error) {
	if request == nil {
		request = NewDescribeRecordRequest()
	}
	request.Init().SetPath(fmt.Sprintf(recordPath, c.DatasheetId))
	request.SetHttpMethod(athttp.GET)
	response := NewDescribeRecordResponse()
	err = c.Send(request, response)
	if err != nil {
		return nil, err
	}
	return response.Data, nil
}

// DescribeRecord used to obtain a single record
// * according to `ViewId`, column name, `FieldId` or other information to query the record detailed information.
// * For details of filtering information please see `RecordRequest`。
// * returns the first record queried
func (c *Datasheet) DescribeRecord(request *DescribeRecordRequest) (record *Record, err error) {
	if request == nil {
		request = NewDescribeRecordRequest()
	}
	request.Init().SetPath(fmt.Sprintf(recordPath, c.DatasheetId))
	request.SetHttpMethod(athttp.GET)
	response := NewDescribeRecordResponse()
	err = c.Send(request, response)
	if err != nil {
		return nil, err
	}
	if len(response.Data.Records) > 0 {
		return response.Data.Records[0], nil
	}
	return nil, nil
}

// CreateRecords used to create multiple records
func (c *Datasheet) CreateRecords(request *CreateRecordsRequest) (records []*Record, err error) {
	if request == nil {
		request = NewCreateRecordsRequest()
	}
	request.Init().SetPath(fmt.Sprintf(recordPath, c.DatasheetId))
	request.SetContentType(athttp.JsonContent)
	response := NewDescribeRecordResponse()
	err = c.Send(request, response)
	if err != nil {
		return nil, err
	}
	return response.Data.Records, nil
}

// ModifyRecords used to modify multiple records
func (c *Datasheet) ModifyRecords(request *ModifyRecordsRequest) (records []*Record, err error) {
	if request == nil {
		request = NewModifyRecordsRequest()
	}
	request.Init().SetPath(fmt.Sprintf(recordPath, c.DatasheetId))
	request.SetContentType(athttp.JsonContent)
	request.SetHttpMethod(athttp.PATCH)
	response := NewDescribeRecordResponse()
	err = c.Send(request, response)
	if err != nil {
		return nil, err
	}
	return response.Data.Records, nil
}

// DeleteRecords used to delete multiple records
func (c *Datasheet) DeleteRecords(request *DeleteRecordsRequest) (err error) {
	if request == nil {
		request = NewDeleteRecordsRequest()
	}
	request.Init().SetPath(fmt.Sprintf(recordPath, c.DatasheetId))
	request.SetHttpMethod(athttp.DELETE)
	response := NewDescribeRecordResponse()
	err = c.Send(request, response)
	return
}

// UploadFile used to upload attachments
func (c *Datasheet) UploadFile(request *UploadRequest) (attachment *Attachment, err error) {
	if request == nil {
		request = NewUploadRequest()
	}
	body, contentType, err := common.FileBuffer(request.FilePath)
	if err != nil {
		return
	}
	request.Init()
	request.SetPath(fmt.Sprintf(attachPath, c.DatasheetId))
	request.SetHttpMethod(athttp.POST)
	request.SetFile(body)
	request.SetContentType(contentType)
	response := NewUploadResponse()
	err = c.Send(request, response)
	if err != nil {
		return nil, err
	}
	return response.Data, nil
}
