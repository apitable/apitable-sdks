package datasheet

import (
	"fmt"
	athttp "github.com/apitable/apitable-sdks/apitable.go/lib/common/http"
)

const viewPath = "/fusion/v1/datasheets/%s/views"

// ViewType the type of the view
type ViewType string

// All of view types
const (
	ViewType_Grid    = "Grid"
	ViewType_Gallery = "Gallery"
	ViewType_Kanban  = "Kanban"
	ViewType_Gantt   = "Gantt"
)

// DatasheetView describe the views of table
type DatasheetView struct {
	// Id view id
	Id *string `json:"id,omitempty" name:"id"`
	// Name view name
	Name *string `json:"name,omitempty" name:"name"`
	// Type view type
	Type *ViewType `json:"type,omitempty" name:"type"`
}

func NewDescribeViewsRequest() (request *DescribeViewsRequest) {
	request = &DescribeViewsRequest{
		BaseRequest: &athttp.BaseRequest{},
	}
	return
}

func newDescribeViewsResponse() (response *DescribeViewsResponse) {
	response = &DescribeViewsResponse{
		BaseResponse: &athttp.BaseResponse{},
	}
	return
}

func (c *Datasheet) DescribeViews(request *DescribeViewsRequest) (views []*DatasheetView, err error) {
	if request == nil {
		request = NewDescribeViewsRequest()
	}
	request.Init().SetPath(fmt.Sprintf(viewPath, c.DatasheetId))
	request.SetHttpMethod(athttp.GET)
	response := newDescribeViewsResponse()
	err = c.Send(request, response)
	if err != nil {
		return nil, err
	}
	return response.Data.Views, nil
}
