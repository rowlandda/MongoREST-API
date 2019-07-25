#!/bin/bash
docker run -d --name mongodb -p 27017:27017 -v ~/data:/data/db mongo
python3 MongoUtil.py all delete
python3 MongoUtil.py all create
mvn clean install
mvn spring-boot:run
