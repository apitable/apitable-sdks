package test

import (
	"fmt"
	"github.com/apitable/apitable-sdks/apitable.go/lib/common"
	aterror "github.com/apitable/apitable-sdks/apitable.go/lib/common/error"
	"github.com/apitable/apitable-sdks/apitable.go/lib/common/profile"
	"github.com/apitable/apitable-sdks/apitable.go/lib/common/util"
	apitable "github.com/apitable/apitable-sdks/apitable.go/lib/datasheet"
	"github.com/apitable/apitable-sdks/apitable.go/lib/space"
	"os"
	"testing"
)

func TestCreateRecords(t *testing.T) {
	// HOST can use the produced host by default without setting.
	credential := common.NewCredential(os.Getenv("TOKEN"))
	cpf := profile.NewClientProfile()
	cpf.HttpProfile.Domain = os.Getenv("DOMAIN")
	datasheet, _ := apitable.NewDatasheet(credential, os.Getenv("DATASHEET_ID"), cpf)
	t.Log("DATASHEET_ID", os.Getenv("DATASHEET_ID"))
	request := apitable.NewCreateRecordsRequest()
	request.Records = []*apitable.Fields{
		{
			Fields: &apitable.Field{
				os.Getenv("NUMBER_FIELD_NAME"): apitable.NumberFieldValue(900),
			},
		},
	}
	records, err := datasheet.CreateRecords(request)
	if _, ok := err.(*aterror.APITableError); ok {
		t.Errorf("An API error has returned: %s", err)
	}
	// Non-SDK exception, direct failure. Other processing can be added to the actual code.
	if err != nil {
		t.Errorf("An unexcepted error has returned: %s", err)
		panic(err)
	}
	t.Log(len(records))
}

func TestDescribeAllRecords(t *testing.T) {
	// HOST can use the produced host by default without setting.
	credential := common.NewCredential(os.Getenv("TOKEN"))
	cpf := profile.NewClientProfile()
	cpf.HttpProfile.Domain = os.Getenv("DOMAIN")
	datasheet, _ := apitable.NewDatasheet(credential, os.Getenv("DATASHEET_ID"), cpf)
	request := apitable.NewDescribeRecordRequest()
	request.Sort = []*apitable.Sort{
		{
			Field: common.StringPtr(os.Getenv("NUMBER_FIELD_NAME")),
			Order: common.StringPtr("desc"),
		},
	}
	request.Fields = common.StringPtrs([]string{os.Getenv("NUMBER_FIELD_NAME")})
	records, err := datasheet.DescribeAllRecords(request)
	if _, ok := err.(*aterror.APITableError); ok {
		t.Errorf("An API error has returned: %s", err)
	}
	// Non-SDK exception, direct failure. Other processing can be added to the actual code.
	if err != nil {
		t.Errorf("An unexcepted error has returned: %s", err)
		panic(err)
	}
	t.Log(len(records))
}

func TestDescribeRecords(t *testing.T) {
	// HOST can use the produced host by default without setting.
	credential := common.NewCredential(os.Getenv("TOKEN"))
	cpf := profile.NewClientProfile()
	cpf.HttpProfile.Domain = os.Getenv("DOMAIN")
	datasheet, _ := apitable.NewDatasheet(credential, os.Getenv("DATASHEET_ID"), cpf)
	request := apitable.NewDescribeRecordRequest()
	request.Sort = []*apitable.Sort{
		{
			Field: common.StringPtr(os.Getenv("NUMBER_FIELD_NAME")),
			Order: common.StringPtr("desc"),
		},
	}
	request.Fields = common.StringPtrs([]string{os.Getenv("NUMBER_FIELD_NAME")})
	records, err := datasheet.DescribeRecords(request)
	if _, ok := err.(*aterror.APITableError); ok {
		t.Errorf("An API error has returned: %s", err)
	}
	// Non-SDK exception, direct failure. Other processing can be added to the actual code.
	if err != nil {
		t.Errorf("An unexcepted error has returned: %s", err)
		panic(err)
	}
	t.Log(len(records.Records))
}

func TestModifyRecords(t *testing.T) {
	// HOST can use the produced host by default without setting.
	credential := common.NewCredential(os.Getenv("TOKEN"))
	cpf := profile.NewClientProfile()
	cpf.HttpProfile.Domain = os.Getenv("DOMAIN")
	datasheet, _ := apitable.NewDatasheet(credential, os.Getenv("DATASHEET_ID"), cpf)
	describeRequest := apitable.NewDescribeRecordRequest()
	describeRequest.FilterByFormula = common.StringPtr("{" + os.Getenv("NUMBER_FIELD_NAME") + "}=900")
	record, _ := datasheet.DescribeRecord(describeRequest)
	request := apitable.NewModifyRecordsRequest()
	request.Records = []*apitable.BaseRecord{
		{
			Fields: &apitable.Field{
				os.Getenv("NUMBER_FIELD_NAME"): apitable.NumberFieldValue(1000),
			},
			RecordId: record.RecordId,
		},
	}
	records, err := datasheet.ModifyRecords(request)
	if _, ok := err.(*aterror.APITableError); ok {
		t.Errorf("An API error has returned: %s", err)
	}
	// Non-SDK exception, direct failure. Other processing can be added to the actual code.
	if err != nil {
		t.Errorf("An unexcepted error has returned: %s", err)
		panic(err)
	}
	t.Log(len(records))
}

func TestDeleteRecords(t *testing.T) {
	// HOST can use the produced host by default without setting.
	credential := common.NewCredential(os.Getenv("TOKEN"))
	cpf := profile.NewClientProfile()
	cpf.HttpProfile.Domain = os.Getenv("DOMAIN")
	datasheet, _ := apitable.NewDatasheet(credential, os.Getenv("DATASHEET_ID"), cpf)
	describeRequest := apitable.NewDescribeRecordRequest()
	describeRequest.FilterByFormula = common.StringPtr("{" + os.Getenv("NUMBER_FIELD_NAME") + "}=1000")
	record, _ := datasheet.DescribeRecord(describeRequest)
	request := apitable.NewDeleteRecordsRequest()
	request.RecordIds = []*string{record.RecordId}
	err := datasheet.DeleteRecords(request)
	if _, ok := err.(*aterror.APITableError); ok {
		fmt.Printf("An API error has returned: %s", err)
		return
	}
	// Non-SDK exception, direct failure. Other processing can be added to the actual code.
	if err != nil {
		t.Errorf("An unexcepted error has returned: %s", err)
		panic(err)
	}
}

func TestUpload(t *testing.T) {
	// HOST can use the produced host by default without setting.
	credential := common.NewCredential(os.Getenv("TOKEN"))
	cpf := profile.NewClientProfile()
	cpf.HttpProfile.Domain = os.Getenv("DOMAIN")
	cpf.Upload = true
	datasheet, _ := apitable.NewDatasheet(credential, os.Getenv("DATASHEET_ID"), cpf)
	request := apitable.NewUploadRequest()
	request.FilePath = "image.png"
	attachment, err := datasheet.UploadFile(request)
	if _, ok := err.(*aterror.APITableError); ok {
		fmt.Printf("An API error has returned: %s", err)
		return
	}
	// Non-SDK exception, direct failure. Other processing can be added to the actual code.
	if err != nil {
		t.Errorf("An unexcepted error has returned: %s", err)
		panic(err)
	}
	t.Log(attachment)
}

func TestDescribeFields(t *testing.T) {
	// HOST can use the produced host by default without setting.
	credential := common.NewCredential(os.Getenv("TOKEN"))
	cpf := profile.NewClientProfile()
	cpf.HttpProfile.Domain = os.Getenv("DOMAIN")
	datasheet, _ := apitable.NewDatasheet(credential, os.Getenv("DATASHEET_ID"), cpf)
	describeRequest := apitable.NewDescribeFieldsRequest()
	describeRequest.ViewId = common.StringPtr(os.Getenv("VIEW_ID"))
	fields, err := datasheet.DescribeFields(describeRequest)
	if _, ok := err.(*aterror.APITableError); ok {
		t.Errorf("An API error has returned: %s", err)
	}
	// Non-SDK exception, direct failure. Other processing can be added to the actual code.
	if err != nil {
		t.Errorf("An unexcepted error has returned: %s", err)
		panic(err)
	}
	for _, value := range fields {
		property := value.SelectFieldProperty()
		util.Dd(property)
	}
	t.Log(len(fields))
}

func TestDescribeViews(t *testing.T) {
	// HOST can use the produced host by default without setting.
	credential := common.NewCredential(os.Getenv("TOKEN"))
	cpf := profile.NewClientProfile()
	cpf.HttpProfile.Domain = os.Getenv("DOMAIN")
	datasheet, _ := apitable.NewDatasheet(credential, os.Getenv("DATASHEET_ID"), cpf)
	describeRequest := apitable.NewDescribeViewsRequest()
	views, err := datasheet.DescribeViews(describeRequest)
	if _, ok := err.(*aterror.APITableError); ok {
		t.Errorf("An API error has returned: %s", err)
	}
	// Non-SDK exception, direct failure. Other processing can be added to the actual code.
	if err != nil {
		t.Errorf("An unexcepted error has returned: %s", err)
		panic(err)
	}
	util.Dd(views)
	t.Log(len(views))
}

func TestDescribeSpaces(t *testing.T) {
	// HOST can use the produced host by default without setting.
	credential := common.NewCredential(os.Getenv("TOKEN"))
	cpf := profile.NewClientProfile()
	cpf.HttpProfile.Domain = os.Getenv("DOMAIN")
	spaceClient, _ := space.NewSpace(credential, "", cpf)
	describeRequest := space.NewDescribeSpacesRequest()
	spaces, err := spaceClient.DescribeSpaces(describeRequest)
	if _, ok := err.(*aterror.APITableError); ok {
		t.Errorf("An API error has returned: %s", err)
	}
	// Non-SDK exception, direct failure. Other processing can be added to the actual code.
	if err != nil {
		t.Errorf("An unexcepted error has returned: %s", err)
		panic(err)
	}
	util.Dd(spaces)
	t.Log(len(spaces))
}

func TestDescribeNodes(t *testing.T) {
	// HOST can use the produced host by default without setting.
	credential := common.NewCredential(os.Getenv("TOKEN"))
	cpf := profile.NewClientProfile()
	cpf.HttpProfile.Domain = os.Getenv("DOMAIN")
	spaceClient, _ := space.NewSpace(credential, os.Getenv("SPACE_ID"), cpf)
	describeRequest := space.NewDescribeNodesRequest()
	nodes, err := spaceClient.DescribeNodes(describeRequest)
	if _, ok := err.(*aterror.APITableError); ok {
		t.Errorf("An API error has returned: %s", err)
	}
	// Non-SDK exception, direct failure. Other processing can be added to the actual code.
	if err != nil {
		t.Errorf("An unexcepted error has returned: %s", err)
		panic(err)
	}
	util.Dd(nodes)
	t.Log(len(nodes))
}

func TestDescribeNode(t *testing.T) {
	// HOST can use the produced host by default without setting.
	credential := common.NewCredential(os.Getenv("TOKEN"))
	cpf := profile.NewClientProfile()
	cpf.HttpProfile.Domain = os.Getenv("DOMAIN")
	spaceClient, _ := space.NewSpace(credential, os.Getenv("SPACE_ID"), cpf)
	describeRequest := space.NewDescribeNodeRequest()
	describeRequest.NodeId = common.StringPtr(os.Getenv("DATASHEET_ID"))
	node, err := spaceClient.DescribeNode(describeRequest)
	if _, ok := err.(*aterror.APITableError); ok {
		t.Errorf("An API error has returned: %s", err)
	}
	// Non-SDK exception, direct failure. Other processing can be added to the actual code.
	if err != nil {
		t.Errorf("An unexcepted error has returned: %s", err)
		panic(err)
	}
	util.Dd(node)
	t.Log(node)
}
