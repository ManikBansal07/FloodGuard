#!/bin/bash

# Download Maven Wrapper
mvn -N wrapper:wrapper

# Make the wrapper executable
chmod +x mvnw
chmod +x mvnw.cmd 