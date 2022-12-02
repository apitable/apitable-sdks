package profile

type HttpProfile struct {
	ReqMethod  string
	ReqTimeout int
	Scheme     string
	Domain     string
	UserAgent  string
}

func NewHttpProfile() *HttpProfile {
	return &HttpProfile{
		ReqMethod:  "POST",
		ReqTimeout: 60,
		Scheme:     "HTTPS",
		Domain:     "",
		UserAgent:  "go-sdk",
	}
}
