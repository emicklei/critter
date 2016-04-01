package main

import (
	"net/http"
	"os"

	. "github.com/emicklei/forest"
)

const someComplex = "CgpoZWxsbyBzb21lEAAYKiXD9UhAKNKF2MwEMCowVDoCCERCAggXSgIIAFINCgtoZWxsbyBwcm90b1oICgYICBAmGCpiCQoHCOAPEAMYEmoFdG9rZW5yEwoRCgcI4A8QDBgUEgYICBApGCqCAQdtaXNzaW5nigEKCgNvbmUKA3R3b5IBCQoHCgUIoQEQApoBBUhFTExPogECCCqqAQcKBXRocmVl"

func main() {
	t := TestingT
	api := NewClient("http://localhost:8877", new(http.Client))
	f, _ := os.Open("../src/test/resources/bol-xsdtypes-1.6.proto")
	defer f.Close()
	cfg := NewConfig("/rules/fixed-1/proto")
	cfg.BodyReader = f
	r := api.POST(t, cfg)
	Dump(t, r)

	cfg = NewConfig("/rules/fixed-1/proto/xsdtypes.DayTime")
	r = api.GET(t, cfg)
	Dump(t, r)
}
