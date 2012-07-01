## Critter is a http proxy server for simulating connectivity and transport problems when sending http requests.

Critter provides two services: proxy service and a traffic control service.

* The proxy service is a pass-through server that usually sits between a http client and a http server.

* The traffic control service is used to change the behavior of the proxy service with respect to request and response handling.
Information about what to do with a request or a response is available through a traffic manager.

Critter uses rules (xml) to specify what should happen to a request that it receives or response that it returns.
For each incoming http request, the proxy service will ask the traffic manager for a rule that matches that request.

* A rule matches if all its conditions are met. A rule can use conditions that look at the host, port, path, header or a combination thereof. If such a rule is found then all its actions are performed. 

* A rule can have actions that change the request, headers, body or those of the response.
All its actions are performed in the order as listed in the rule.

* A rule has a name and can be disabled. Rules are defined in XML.
Rules are managed (crud) using the REST interface of the traffic service.

The proxy service itself can be terminated and started using the traffic service.

2012 (c) Ernest Micklei, Apache 2 Licence
