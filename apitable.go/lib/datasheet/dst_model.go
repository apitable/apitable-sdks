package datasheet

import (
	"encoding/json"
	athttp "github.com/apitable/apitable-sdks/apitable.go/lib/common/http"
)

func (r *DescribeRecordRequest) ToJsonString() string {
	b, _ := json.Marshal(r)
	return string(b)
}

func (r *DescribeRecordRequest) FromJsonString(s string) error {
	return json.Unmarshal([]byte(s), &r)
}

type (
	FieldValue       interface{}
	NumberFieldValue int64
	TextFieldValue   string
	UnitFieldValue   struct {
		UnitName string `json:"unitName,omitempty" name:"unitName"`
		UnitType string `json:"unitType,omitempty" name:"unitType"`
		UnitId   string `json:"unitId,omitempty" name:"unitId"`
	}
	AttachmentValue struct {
		Token string `json:"token,omitempty" name:"token"`
		Name  string `json:"name,omitempty" name:"name"`
	}
)

type (
	Field map[string]FieldValue
)

// SingleTextFieldProperty describe the single text field property
type SingleTextFieldProperty struct {
	DefaultValue string `json:"defaultValue,omitempty" name:"defaultValue"`
}

// SelectFieldProperty describe the single select field and multi select field property
type SelectFieldProperty struct {
	Options []*SelectFieldOption `json:"options,omitempty" name:"options"`
}

// NumberFieldProperty describe number field property
type NumberFieldProperty struct {
	DefaultValue *string `json:"defaultValue,omitempty" name:"defaultValue"`
	Precision    *int    `json:"precision,omitempty" name:"precision"`
}

// CurrencyFieldProperty describe currency field property
type CurrencyFieldProperty struct {
	DefaultValue *string `json:"defaultValue,omitempty" name:"defaultValue"`
	Precision    *int    `json:"precision,omitempty" name:"precision"`
	Symbol       *string `json:"symbol,omitempty" name:"symbol"`
}

// PercentFieldProperty describe percent field property
type PercentFieldProperty struct {
	DefaultValue *string `json:"defaultValue,omitempty" name:"defaultValue"`
	Precision    *int    `json:"precision,omitempty" name:"precision"`
}

// DateTimeFieldProperty describe date time field property
type DateTimeFieldProperty struct {
	Format      *string `json:"format,omitempty" name:"format"`
	IncludeTime *bool   `json:"includeTime,omitempty" name:"includeTime"`
	AutoFill    *bool   `json:"autoFill,omitempty" name:"autoFill"`
}

// MemberFieldProperty describe member field property
type MemberFieldProperty struct {
	Options       []*MemberFieldOption `json:"options,omitempty" name:"options"`
	IsMulti       *bool                `json:"isMulti,omitempty" name:"isMulti"`
	ShouldSendMsg *bool                `json:"shouldSendMsg,omitempty" name:"shouldSendMsg"`
}

// CheckboxFieldProperty describe checkbox field property
type CheckboxFieldProperty struct {
	Icon *string `json:"icon,omitempty" name:"icon"`
}

// RatingFieldProperty describe rating field property
type RatingFieldProperty struct {
	Icon *string `json:"icon,omitempty" name:"icon"`
	Max  *int    `json:"max,omitempty" name:"max"`
}

// MagicLinkFieldProperty describe magic link field property
type MagicLinkFieldProperty struct {
	ForeignDatasheetId *string `json:"foreignDatasheetId,omitempty" name:"foreignDatasheetId"`
	BrotherFieldId     *string `json:"brotherFieldId,omitempty" name:"brotherFieldId"`
	LimitToViewId      *string `json:"limitToViewId,omitempty" name:"limitToViewId"`
	LimitSingleRecord  *bool   `json:"limitSingleRecord,omitempty" name:"limitSingleRecord"`
}

// MagicLookUpFieldProperty describe magic lookup field property
type MagicLookUpFieldProperty struct {
	RelatedLinkFieldId *string            `json:"relatedLinkFieldId,omitempty" name:"relatedLinkFieldId"`
	TargetFieldId      *string            `json:"targetFieldId,omitempty" name:"targetFieldId"`
	RollupFunction     *RollupFunction    `json:"rollupFunction,omitempty" name:"rollupFunction"`
	ValueType          *ValueType         `json:"valueType,omitempty" name:"valueType"`
	EntityField        *LookUpFieldEntity `json:"entityField,omitempty" name:"entityField"`
	Format             *FieldFormat       `json:"format,omitempty" name:"format"`
}

// FormulaFieldProperty describe formula field property
type FormulaFieldProperty struct {
	Expression *string      `json:"expression,omitempty" name:"expression"`
	ValueType  *ValueType   `json:"valueType,omitempty" name:"valueType"`
	HasError   *bool        `json:"hasError,omitempty" name:"hasError"`
	Format     *FieldFormat `json:"format,omitempty" name:"format"`
}

// UserFieldProperty describe createdBy,lastModifiedBy field property
type UserFieldProperty struct {
	Options []*UserInfo `json:"options,omitempty" name:"options"`
}

// SelectFieldOption describe the single select field and multi select field option property
type SelectFieldOption struct {
	Id    *string                 `json:"id,omitempty" name:"id"`
	Name  *string                 `json:"name,omitempty" name:"name"`
	Color *SelectFieldOptionColor `json:"color,omitempty"`
}

// MemberFieldOption describe the member field option property
type MemberFieldOption struct {
	Id     *string     `json:"id,omitempty" name:"id"`
	Name   *string     `json:"name,omitempty" name:"name"`
	Type   *MemberType `json:"type,omitempty" name:"type"`
	Avatar *string     `json:"avatar,omitempty" name:"avatar"`
}

// UserInfo describe the user base info
type UserInfo struct {
	Id     *string `json:"id,omitempty" name:"id"`
	Name   *string `json:"name,omitempty" name:"name"`
	Avatar *string `json:"avatar,omitempty" name:"avatar"`
}

// SelectFieldOptionColor describe the single select field and multi select field option's color property
type SelectFieldOptionColor struct {
	Name  *string `json:"name,omitempty" name:"name"`
	Value *string `json:"value,omitempty" name:"value"`
}

type LookUpFieldEntity struct {
	DatasheetId *string         `json:"datasheetId,omitempty" name:"datasheetId"`
	Field       *DatasheetField `json:"field,omitempty" name:"field"`
}

type LookUpFieldFormat struct {
	Type   *string         `json:"type,omitempty" name:"type"`
	Format *DatasheetField `json:"field,omitempty" name:"field"`
}

// FieldFormat the format of the field set for the record value to show
type FieldFormat struct {
	DateTimeFieldFormat
	NumberFieldFormat
	CurrencyFieldFormat
}

// DateTimeFieldFormat the format for make the value just like the datetime filed shows
type DateTimeFieldFormat struct {
	DateFormat  *string `json:"dateFormat,omitempty" name:"dateFormat"`
	TimeFormat  *string `json:"timeFormat,omitempty" name:"timeFormat"`
	IncludeTime *bool   `json:"includeTime,omitempty" name:"includeTime"`
}

// NumberFieldFormat the format for make the value just like the number filed shows
type NumberFieldFormat struct {
	Precision *int `json:"precision,omitempty" name:"precision"`
}

// CurrencyFieldFormat the format for make the value just like the currency filed shows
type CurrencyFieldFormat struct {
	Precision *int `json:"precision,omitempty" name:"precision"`
	Symbol    *int `json:"symbol,omitempty" name:"symbol"`
}

// MemberType the apitable datasheet member field type
type MemberType string

// ValueType the apitable datasheet basic value type
type ValueType string

// RollupFunction the apitable datasheet supported customize function
type RollupFunction string

// all datasheet member field types
const (
	MemberType_Member MemberType = "Member"
	MemberType_Team   MemberType = "Team"
)

// datasheet supported basic value type
const (
	ValueType_String   ValueType = "String"
	ValueType_Boolean  ValueType = "Boolean"
	ValueType_Number   ValueType = "Number"
	ValueType_DateTime ValueType = "DateTime"
	ValueType_Array    ValueType = "Array"
)

// datasheet supported all rollup functions
const (
	RollupFunction_VALUES       RollupFunction = "VALUES"
	RollupFunction_AVERAGE      RollupFunction = "AVERAGE"
	RollupFunction_COUNT        RollupFunction = "COUNT"
	RollupFunction_COUNTA       RollupFunction = "COUNTA"
	RollupFunction_COUNTALL     RollupFunction = "COUNTALL"
	RollupFunction_SUM          RollupFunction = "SUM"
	RollupFunction_MIN          RollupFunction = "MIN"
	RollupFunction_MAX          RollupFunction = "MAX"
	RollupFunction_AND          RollupFunction = "AND"
	RollupFunction_OR           RollupFunction = "OR"
	RollupFunction_XOR          RollupFunction = "XOR"
	RollupFunction_CONCATENATE  RollupFunction = "CONCATENATE"
	RollupFunction_ARRAYJOIN    RollupFunction = "ARRAYJOIN"
	RollupFunction_ARRAYUNIQUE  RollupFunction = "ARRAYUNIQUE"
	RollupFunction_ARRAYCOMPACT RollupFunction = "ARRAYCOMPACT"
)

// Sort the need sorted fields
type Sort struct {

	// the need sorted fields' name
	Field *string `json:"Field,omitempty" name:"field"`

	// sort order desc/asc
	Order *string `json:"Order,omitempty" name:"order"`
}

type DescribeRecordRequest struct {
	*athttp.BaseRequest

	// query by one or more record id. record id such as：`rec*****`.
	//（For the specific format of this parameter, please refer to the api [developer documentation](https://help.apitable.com/api-get-records/)）。
	// The maximum number of instances per request is 100.
	// The parameter does not support specifying both 'Record Ids' and 'Filters'.
	RecordIds []*string `json:"recordIds,omitempty" name:"recordIds" list`

	// filter by view. value such as: viw*****. required: no.
	ViewId *string `json:"viewId,omitempty" name:"viewId"`

	// filter by field. value such as: fld*****. required: no.
	Fields []*string `json:"fields,omitempty" name:"fields"`

	// filter by formula. value such as: max({field}). required: no.
	// [one minute hands on formula](https://help.apitable.com/tutorial-getting-started-with-formulas/).
	FilterByFormula *string `json:"filterByFormula,omitempty" name:"filterByFormula"`

	// filter by [cell value type], the default is json. When specified as string, all values will be automatically converted to string format. such as: json. required: no.
	CellFormat *string `json:"cellFormat,omitempty" name:"cellFormat"`

	// filter by column identification. By default, the column name is used.value such as: name. required: no.
	FieldKey *string `json:"fieldKey,omitempty" name:"fieldKey"`

	// The parameter does not support specifying both 'Record Ids' and 'Filters'.
	// filter by sort. such as：{field: ‘field_name’, order: ‘asc/desc’}. required: no.
	Sort []*Sort `json:"sort,omitempty" name:"sort" list`

	// Specifies the page number of the page. The default is 1. It is used in conjunction with the parameter page size. [more see](https://help.apitable.com/api-get-records/)
	PageNum *int64 `json:"pageNum,omitempty" name:"pageNum"`

	// The number page returned. The default value is 100. The maximum value is 1000. [more see](https://help.apitable.com/api-get-records/).
	PageSize *int64 `json:"pageSize,omitempty" name:"pageSize"`

	// The number record returned. [more see](https://help.apitable.com/api-get-records/)
	MaxRecords *int64 `json:"maxRecords,omitempty" name:"maxRecords"`
}

type Fields struct {
	Fields *Field `json:"fields,omitempty" name:"fields" map`
}

type BaseRecord struct {
	// such: `rec*****`
	RecordId *string `json:"recordId,omitempty" name:"recordId"`
	// key/value corresponding to column
	Fields *Field `json:"fields,omitempty" name:"fields" map`
}

type Record struct {
	*BaseRecord
	// record creation time. such as: timestamp
	CreatedAt *int64 `json:"createdAt,omitempty" name:"createdAt"`
}

type CreateRecordsRequest struct {
	*athttp.BaseRequest
	// key/value corresponding to column
	Records []*Fields `json:"records,omitempty" name:"records" map`
}

type ModifyRecordsRequest struct {
	*athttp.BaseRequest
	// key/value corresponding to column
	Records []*BaseRecord `json:"records,omitempty" name:"records" map`
}

type DeleteRecordsRequest struct {
	*athttp.BaseRequest
	// key/value corresponding to column
	RecordIds []*string `json:"recordIds,omitempty" name:"recordIds" list`
}

type UploadRequest struct {
	*athttp.BaseRequest
	// file path
	FilePath string `json:"filePath,omitempty" name:"filePath" string`
}

type DescribeFieldsRequest struct {
	*athttp.BaseRequest
	// filter by view. value such as: viw*****. required: no.
	ViewId *string `json:"viewId,omitempty" name:"viewId"`
}

type DescribeViewsRequest struct {
	*athttp.BaseRequest
}

type RecordPagination struct {
	// current number of pages
	PageNum *int64 `json:"pageNum,omitempty" name:"pageNum"`

	PageSize *int64 `json:"pageSize,omitempty" name:"pageSize"`

	Total *int64 `json:"total,omitempty" name:"total"`

	Records []*Record `json:"records"`
}

type Attachment struct {
	// attachment unique identification
	Token *string `json:"token,omitempty" name:"token"`
	// attachment original name
	Name *string `json:"name,omitempty" name:"name"`
	// attachment size
	Size *int64 `json:"size,omitempty" name:"size"`
	// when the attachment is a picture, the width of the picture.
	Width *int64 `json:"width,omitempty" name:"width"`
	// when the attachment is a picture, the height of the picture.
	Height *int64 `json:"height,omitempty" name:"height"`
	// attachment type, such as：image/jpeg
	MimeType *string `json:"mimeType,omitempty" name:"mimeType"`
	// pdf preview image, only pdf format will be returned
	Preview *string `json:"preview,omitempty" name:"preview"`
	// attachment access path
	Url *string `json:"url,omitempty" name:"url"`
}

type DescribeRecordResponse struct {
	*athttp.BaseResponse
	// api response data
	Data *RecordPagination `json:"data"`
}

type UploadResponse struct {
	*athttp.BaseResponse
	// api response data
	Data *Attachment `json:"data"`
}

type FieldsResponse struct {
	Fields []*DatasheetField `json:"fields"`
}

type ViewsResponse struct {
	Views []*DatasheetView `json:"views"`
}

type DescribeFieldsResponse struct {
	*athttp.BaseResponse
	// api response data
	Data *FieldsResponse `json:"data"`
}

type DescribeViewsResponse struct {
	*athttp.BaseResponse
	// api response data
	Data *ViewsResponse `json:"data"`
}
