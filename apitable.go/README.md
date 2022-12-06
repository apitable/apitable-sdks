# APITable

[APITable](https://apitable.com) Golang SDK is the encapsulation of the APITable API.

## Quickstart

### Environmental requirements

go 1.15 +  

### Install dependency

```shell
go get github.com/apitable/apitable-sdks/apitable.go
```

## Get API TOKEN

Visit the workbench of apitable, click on the personal avatar in the lower left corner, and enter [My Setting > Developer]. Click generate token (binding email required for first use).

### Use

```go
package main

import (
    "fmt"
    "github.com/apitable/apitable-sdks/apitable.go/lib/common"
    aterror "github.com/apitable/apitable-sdks/apitable.go/lib/common/error"
    "github.com/apitable/apitable-sdks/apitable.go/lib/common/profile"
	apitable "github.com/apitable/apitable-sdks/apitable.go/lib/datasheet"
)

func main() {
    credential := common.NewCredential("YOUR_API_TOKEN")
    cpf := profile.NewClientProfile()
    datasheet, _ := apitable.NewDatasheet(credential, "datasheetId", cpf)
    // get all the data
    request := apitable.NewDescribeRecordRequest()
    request.Sort = []*apitable.Sort{
        {
            Field: common.StringPtr("number_field"),
            Order: common.StringPtr("desc"),
        },
    }
    request.Fields = common.StringPtrs([]string{"number_field"})
    records, err := datasheet.DescribeAllRecords(request)
    if _, ok := err.(*aterror.SDKError); ok {
       fmt.Printf("An API error has returned: %s", err)
       return
    }
    // Non-SDK exception, direct failure. Other processing can be added to the actual code.
    if err != nil {
        panic(err)
    }
    // print the returned data
    fmt.Printf("%#v\n", records)
    // paging to get data
    page, err := datasheet.DescribeRecords(request)
	if _, ok := err.(*aterror.SDKError); ok {
       fmt.Printf("An API error has returned: %s", err)
       return
    }
	// Non-SDK exception, direct failure. Other processing can be added to the actual code.
	if err != nil {
        panic(err)
    }
    // print the returned data
    fmt.Printf("%#v\n", page)
    // add record
    createRequest := apitable.NewCreateRecordsRequest()
    createRequest.Records = []*apitable.Fields{
        {
            Fields: &apitable.Field{
                "number_field": apitable.NumberFieldValue(900),
            },
        },
    }
    createRecords, err := datasheet.CreateRecords(createRequest)
    if _, ok := err.(*aterror.SDKError); ok {
       fmt.Printf("An API error has returned: %s", err)
       return
    }
	// Non-SDK exception, direct failure. Other processing can be added to the actual code.
	if err != nil {
        panic(err)
    }
    // print the returned data
    fmt.Printf("%#v\n", createRecords)
	// modify record
    modifyRequest := apitable.NewModifyRecordsRequest()
    modifyRequest.Records = []*apitable.BaseRecord{
        {
            Fields: &apitable.Field{
                "number_field": apitable.NumberFieldValue(1000),
            },
            RecordId: common.StringPtr("recordId"),
        },
    }
    modifyRecords, err := datasheet.ModifyRecords(modifyRequest)
    if _, ok := err.(*aterror.SDKError); ok {
       fmt.Printf("An API error has returned: %s", err)
       return
    }
	// Non-SDK exception, direct failure. Other processing can be added to the actual code.
	if err != nil {
        panic(err)
    }
	// print the returned data
    fmt.Printf("%#v\n", modifyRecords)
	// delete record
    deleteRequest := apitable.NewDeleteRecordsRequest()
    request.RecordIds =	common.StringPtrs([]string{"recordId1", "recordId2"})
    err = datasheet.DeleteRecords(deleteRequest)
    if _, ok := err.(*aterror.SDKError); ok {
       fmt.Printf("An API error has returned: %s", err)
       return
    }
	// Non-SDK exception, direct failure. Other processing can be added to the actual code.
	if err != nil {
        panic(err)
    }
    // upload file
    cpf.Upload = true
    uploadRequest := apitable.NewUploadRequest()
    request.FilePath = "image.png"
    attachment, err := datasheet.UploadFile(request)
    if _, ok := err.(*aterror.SDKError); ok {
       fmt.Printf("An API error has returned: %s", err)
       return
    }
	// Non-SDK exception, direct failure. Other processing can be added to the actual code.
	if err != nil {
        panic(err)
    }
	// print the returned data
	fmt.Printf("%#v\n", attachment)
}

```
