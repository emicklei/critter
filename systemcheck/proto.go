package main

import (
	"net/http"
	"os"

	. "github.com/emicklei/forest"
)

const someComplex = "CgpoZWxsbyBzb21lEAAYKiXD9UhAKNKF2MwEMCowVDoCCERCAggXSgIIAFINCgtoZWxsbyBwcm90b1oICgYICBAmGCpiCQoHCOAPEAMYEmoFdG9rZW5yEwoRCgcI4A8QDBgUEgYICBApGCqCAQdtaXNzaW5nigEKCgNvbmUKA3R3b5IBCQoHCgUIoQEQApoBBUhFTExPogECCCqqAQcKBXRocmVl"

var (
	t   = TestingT
	api = NewClient("http://localhost:8877", new(http.Client))
)

func main() {
	createRule()
	registerProtos()
	reportDayTime()
}

func createRule() {
	r := api.POST(t, NewConfig("/rules/fixed-1/proto").Read(f))
	Dump(t, r)
}

func registerProtos() {
	f, _ := os.Open("../src/test/resources/bol-xsdtypes-1.6.proto")
	defer f.Close()
	r := api.POST(t, NewConfig("/rules/fixed-1/proto").Read(f))
	Dump(t, r)
}

func reportDayTime() {
	cfg = NewConfig("/rules/fixed-1/proto/xsdtypes.DayTime")
	r = api.GET(t, cfg)
	Dump(t, r)
}
