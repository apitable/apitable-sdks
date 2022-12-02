package common

import "time"

// lib sdk global constants
const (
	// return the field key as 'name' which you can say in the web pages
	FieldKeyName string = "name"
	// return the field key as 'id' which you can't say in the web pages
	FieldKeyId string = "id"
	// the sort descending for records
	OrderDesc string = "desc"
	// the sort ascending for records
	OrderAsc string = "asc"
	// the default request host
	DefaultHost string = "https://lib.cn"
	// format the return field value as string
	CellFormatString string = "string"
	// format the return field value as real object
	CellFormatJson string = "json"
	// the default per page records count
	DefaultPageSize int = 100
	// the max count for get get records
	MaxPageSize int = 1000
	// the default page for paged records
	DefaultPageNum int = 1
	// the default request timeout
	DefaultRequestTimeout time.Duration = 60000000000
)
