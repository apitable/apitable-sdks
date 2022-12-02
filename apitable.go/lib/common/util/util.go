package util

import (
	"bytes"
	"encoding/json"
	"fmt"
	"reflect"
)

func Dd(data interface{}) {
	bs, _ := json.Marshal(data)
	var out bytes.Buffer
	_ = json.Indent(&out, bs, "", "\t")
	fmt.Printf("%s=%v\n",  reflect.TypeOf(data), out.String())
}
