// Package common provides basic method and struct definition
package common

func Int64Ptr(v int64) *int64 {
	return &v
}

func StringPtr(v string) *string {
	return &v
}

func StringPtrs(vals []string) []*string {
	ptrs := make([]*string, len(vals))
	for i := 0; i < len(vals); i++ {
		ptrs[i] = &vals[i]
	}
	return ptrs
}
