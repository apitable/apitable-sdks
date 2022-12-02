package common

type Common interface {
	NewCredential(token string)
}
type Credential struct {
	Token string
}

func NewCredential(token string) *Credential {
	return &Credential{
		Token: token,
	}
}
