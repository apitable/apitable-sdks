// Package profile provides client common profiles
package profile

type ClientProfile struct {
	HttpProfile *HttpProfile
	FieldKey    string
	Debug       bool
	Upload      bool
}

func NewClientProfile() *ClientProfile {
	return &ClientProfile{
		HttpProfile: NewHttpProfile(),
		FieldKey:    "name",
		Debug:       false,
		Upload:      false,
	}
}
