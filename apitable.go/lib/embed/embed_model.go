package embed

import athttp "github.com/apitable/apitable-sdks/apitable.go/lib/common/http"

type ToolBar struct {
	// The default value is false, whether to display the basic toolbar in the embedded page (including: grouping, filtering, row height, hidden fields, sorting)
	BasicTools *bool `json:"basicTools" name:"basicTools"`
	// The default value is false, and the "Share" button is not displayed in the embedded page
	ShareBtn *bool `json:"shareBtn" name:"shareBtn"`
	// The default value is false, and the "Widget" button is not displayed in the embedded page
	WidgetBtn *bool `json:"widgetBtn" name:"widgetBtn"`
	// The default value is false, the "API" button is not displayed in the embedded page
	ApiBtn *bool `json:"apiBtn" name:"apiBtn"`
	// The default value is false, do not show the "Form" button in the embedded page
	FormBtn *bool `json:"formBtn" name:"formBtn"`
	// The default value is false, do not show the "History" button in the embedded page
	HistoryBtn *bool `json:"historyBtn" name:"historyBtn"`
	// The default value is false, do not show the "Robot" button in the embedded page
	RobotBtn *bool `json:"robotBtn" name:"robotBtn"`
}

type ViewControl struct {
	ViewId *string `json:"viewId,omitempty" name:"viewId"`
	// Whether to display the view tab bar, the default value is true, indicating that the view tab bar is displayed
	TabBar  *bool `json:"tabBar,omitempty" name:"tabBar"`
	ToolBar *bool `json:"toolBar,omitempty" name:"toolBar"`
}

type PrimarySideBar struct {
	// The initial state of the working directory, defaults to false, meaning hidden.
	Collapsed *bool `json:"collapsed,omitempty" name:"collapsed"`
}

type Payload struct {
	PrimarySideBar *PrimarySideBar `json:"primarySideBar,omitempty" name:"primarySideBar"`
	ViewControl    *ViewControl    `json:"viewControl,omitempty" name:"viewControl"`
	// Whether to display the brand logo, the default is true, indicating that the logo is displayed
	BannerLogo *bool `json:"bannerLogo,omitempty" name:"bannerLogo"`
	// readOnly: the default value, if the value of this parameter is not set, the default is read Only. Visitors can only read, not edit, the embedded form.
	//
	// publicEdit: After the visitor login, they can edit the row data in the embedded page.
	//
	// privateEdit: Indicates that the identity of the visitor must be a member of the space station, and the operation authority is the same as the access form inside the space station.
	PermissionType *string `json:"permissionType,omitempty" name:"permissionType"`
}

type CreateEmbedLinkRequest struct {
	*athttp.BaseRequest
	*Link
}

type Link struct {
	// key/value corresponding to column
	Payload *Payload `json:"payload,omitempty" name:"payload"`
	// Specify the theme color, the default value is light, the optional value is: dark | light
	Theme *Payload `json:"theme,omitempty" name:"theme"`
}
