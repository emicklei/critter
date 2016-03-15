# generate protobuf classes for unit tests of protobufpath feature

protoc --proto_path=./src/test/resources --java_out=./src/test/java ./src/test/resources/bol-xsdtypes-1.6.proto
protoc --proto_path=./src/test/resources --java_out=./src/test/java ./src/test/resources/SomeComplexType.proto