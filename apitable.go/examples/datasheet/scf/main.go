package main

import (
	"fmt"
	"github.com/apitable/apitable-sdks/apitable.go/lib/common"
	"github.com/apitable/apitable-sdks/apitable.go/lib/common/profile"
	apitable "github.com/apitable/apitable-sdks/apitable.go/lib/datasheet"
	"github.com/tencentyun/scf-go-lib/cloudfunction"
	"math"
	"os"
)

func getRecords(credential *common.Credential, cpf *profile.ClientProfile) []*apitable.Record {
	datasheet, _ := apitable.NewDatasheet(credential, os.Getenv("DATASHEET_ID"), cpf)
	request := apitable.NewDescribeRecordRequest()
	request.Sort = []*apitable.Sort{
		{
			Field: common.StringPtr(os.Getenv("SORT_FIELD_NAME")),
			Order: common.StringPtr("asc"),
		},
	}
	request.Fields = common.StringPtrs([]string{os.Getenv("SORT_FIELD_NAME")})
	request.PageNum = common.Int64Ptr(1)
	request.ViewId = common.StringPtr(os.Getenv("VIEW_ID"))
	request.PageSize = common.Int64Ptr(100)
	records, err := datasheet.DescribeRecords(request)
	if err != nil {
		fmt.Println("Get records error.")
		panic(err)
	}
	return records.Records
}

func hello() (string, error) {
	// necessary steps：
	// Instantiating an authentication object, the input parameter requires the apitable developer token.
	// Here is the way to read from environment variables.
	// This value needs to be set first in the environment variable.
	// You can also write dead token directly in the code.
	// But be careful not to copy, upload or share the code with others,
	// so as not to disclose the token and endanger the safety of your property.
	credential := common.NewCredential(
		os.Getenv("APITABLE_TOKEN"),
	)
	fmt.Println("Start execution")
	cpf := profile.NewClientProfile()
	records := getRecords(credential, cpf)
	var recordIds []*string
	for i := range records {
		recordIds = append(recordIds, records[i].RecordId)
	}
	fmt.Println("Number of records obtained: ", len(recordIds))
	return deleteRecords(credential, cpf, recordIds)
}

func deleteRecords(credential *common.Credential, cpf *profile.ClientProfile, recordIds []*string) (string, error) {
	datasheet, _ := apitable.NewDatasheet(credential, os.Getenv("DATASHEET_ID"), cpf)
	request := apitable.NewDeleteRecordsRequest()
	for i := 0; i < int(math.Ceil(float64(len(recordIds)/10))); i++ {
		request.RecordIds = recordIds[i*10 : (i+1)*10]
		err := datasheet.DeleteRecords(request)
		if err != nil {
			fmt.Println("Delete failed", i)
		}
		fmt.Println("Delete successful", i)
	}
	return "", nil
}
func main() {
	// Make the handler available for Remote Procedure Call by Cloud Function
	cloudfunction.Start(hello)
}
