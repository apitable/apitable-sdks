package main

import (
	"encoding/json"
	"fmt"
	"github.com/apitable/apitable-sdks/apitable.go/lib/common"
	aterror "github.com/apitable/apitable-sdks/apitable.go/lib/common/error"
	"github.com/apitable/apitable-sdks/apitable.go/lib/common/profile"
	apitable "github.com/apitable/apitable-sdks/apitable.go/lib/datasheet"
	"os"
)

func main() {
	// necessary steps:
	// Instantiating an authentication object, the input parameter requires the apitable developer token.
	// Here is the way to read from environment variables.
	// This value needs to be set first in the environment variable.
	// You can also write dead token directly in the code.
	// But be careful not to copy, upload or share the code with others,
	// so as not to disclose the token and endanger the safety of your property.
	credential := common.NewCredential(
		os.Getenv("APITABLE_TOKEN"),
	)
	cpf := profile.NewClientProfile()
	// upload pictures
	attachment1, err := uploadImage(credential, cpf)
	if _, ok := err.(*aterror.APITableError); ok {
		fmt.Printf("uploadImage:An API error has returned: %s", err)
		return
	}
	// Non-SDK exception, direct failure. Other processing can be added to the actual code.
	if err != nil {
		panic(err)
	}
	json1, _ := json.Marshal(attachment1)
	fmt.Printf("Upload picture results：%s\n", json1)
}

func uploadImage(credential *common.Credential, cpf *profile.ClientProfile) (*apitable.Attachment, error) {
	cpf.Upload = true
	datasheet, _ := apitable.NewDatasheet(credential, os.Getenv("APITABLE_DATASHEET_ID"), cpf)
	request := apitable.NewUploadRequest()
	// If you do not set domain, use the default domain name.
	request.FilePath = "picture full path"
	return datasheet.UploadFile(request)
}
