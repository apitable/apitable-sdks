package embed

import (
	"fmt"
	athttp "github.com/apitable/apitable-sdks/apitable.go/lib/common/http"
)

const embedPath = "/fusion/v1/spaces/%s/nodes/%s/embedlinks"

func NewCreateEmbedLinkRequest() (request *CreateEmbedLinkRequest) {
	request = &CreateEmbedLinkRequest{
		BaseRequest: &athttp.BaseRequest{},
	}
	return
}

// DescribeRecord used to obtain a single record
// * according to `ViewId`, column name, `FieldId` or other information to query the record detailed information.
// * For details of filtering information please see `RecordRequest`。
// * returns the first record queried
func (c *Datasheet) createEmbedLink(request *CreateEmbedLinkRequest) (record *Record, err error) {
	if request == nil {
		request = NewCreateEmbedLinkRequest()
	}
	request.Init().SetPath(fmt.Sprintf(embedPath, c.DatasheetId))
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
