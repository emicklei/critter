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
                 
[![Build Status](https://travis-ci.org/emicklei/critter.png)](https://travis-ci.org/emicklei/critter)

## Installation from source
    
Check out the sources  

	git clone https://github.com/emicklei/critter.git
                     
Build using Maven

	mvn clean install                           

Installation archive is now available

	./target/critter-<version>-pkg.zip

## Installation using distribution binaries
                                             
Unzip the archive

	./bin
	./lib
	./conf	  
          
Modify the configuration

	./conf/critter.properties
	      
Default content is
	
	# proxy.host is the binding address for the http listen sockets
	proxy.host=localhost
	proxy.port=8888
	
	# access to the traffic manager (GUI and REST)
	traffic.port=8877

	# If this property is set then rules are persistent in MongoDB 
	#rule.database.mongo.host=localhost
	#rule.database.mongo.port=27017

	# If this property is enabled, an embedded H2 database is used. 
	# The database file is stored in the critter/bin folder.
	rule.database.h2.enabled=true      

## Start

	sh ./bin/start.sh

## Start using Docker

Build a new image

	mvn clean package docker:build

And run it

	docker run --name critter -P critter/critter:1.4.0

Use `docker ps` to find out to which host ports critter has been bound

## Using a forward proxy for outgoing connections

If you require to connect to the outside world through a proxy, there are multiple options:

* Define the forward proxy in the critter.properties:

    # forward proxy is the proxy that is used to make the outgoing connection.
	forward.http.proxy=http://192.168.128.1:1234
	forward.https.proxy=http://192.168.128.1:1234	
	forward.no.proxy=www.google.nl,www.yahoo.com
	
* Use standard environment variables:

	https_proxy=http://192.168.128.1:1234
    http_proxy=http://192.168.128.1:1234    
    no_proxy=www.google.nl,www.yahoo.com       

* Use non-standard environment variables (where standard environmental variables cannot be used):

    forwardHttpsProxy=http://192.168.128.1:1234
    forwardHttpProxy=http://192.168.128.1:1234
    forwardNoProxy=www.google.nl,www.yahoo.com

## Open Admin user interface

	http://localhost:8877
                         

2013 - 2016 (c) ernestmicklei.com, Apache 2 License
