package main

import (
	"encoding/base64"
	"net/http"
	"net/url"
	"os"

	. "github.com/emicklei/forest"
)

const someComplex = "CgpoZWxsbyBzb21lEAAYKiXD9UhAKNKF2MwEMCowVDoCCERCAggXSgIIAFINCgtoZWxsbyBwcm90b1oICgYICBAmGCpiCQoHCOAPEAMYEmoFdG9rZW5yEwoRCgcI4A8QDBgUEgYICBApGCqCAQdtaXNzaW5nigEKCgNvbmUKA3R3b5IBCQoHCgUIoQEQApoBBUhFTExPogECCCqqAQcKBXRocmVl"

// newDayTime_2016_12_20_8_41_42
const daytime = "ChEKBwjgDxAMGBQSBggIECkYKg=="

var (
	t        = TestingT
	adminApi = NewClient("http://localhost:8877", new(http.Client))
	viaApi   *APITesting
)

func init() {
	proxyUrl, _ := url.Parse("http://localhost:8888")
	withProxy := &http.Client{Transport: &http.Transport{Proxy: http.ProxyURL(proxyUrl)}}
	viaApi = NewClient("http://localhost", withProxy)
}

func main() {
	createDayTimeRule()
	registerXSDTypes("daytime")
	reportDayTime("daytime")

	cfg := NewConfig("/")
	data, _ := base64.StdEncoding.DecodeString(daytime)
	cfg.Content(data, "application/octet-stream")
	r := viaApi.POST(t, cfg)
	Dump(t, r)

	t.Logf("done")
	//registerSomeComplex("somecomplex1")
}

func createDayTimeRule() {
	f, _ := os.Open("../src/test/resources/proto_rule_daytime.xml")
	defer f.Close()
	r := adminApi.POST(t, NewConfig("/rules").Read(f))
	Dump(t, r)
}

func registerXSDTypes(rule string) {
	f, _ := os.Open("../src/test/resources/bol-xsdtypes-1.6.proto")
	defer f.Close()
	r := adminApi.POST(t, NewConfig("/rules/"+rule+"/proto").Read(f))
	Dump(t, r)
}

func registerSomeComplex(rule string) {
	f, _ := os.Open("../src/test/resources/SomeComplexType.proto")
	defer f.Close()
	r := adminApi.POST(t, NewConfig("/rules/"+rule+"/proto").Read(f))
	Dump(t, r)
}

func reportDayTime(rule string) {
	cfg := NewConfig("/rules/" + rule + "/proto/xsdtypes.DayTime")
	r := adminApi.GET(t, cfg)
	Dump(t, r)
}
