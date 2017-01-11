#!/bin/bash
mvn clean && mvn package && mvn install:install-file -Dfile=target/resemble-java-1.0.jar -DpomFile=pom.xml
