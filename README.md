# feedback-api 

## Overview
Feedback REST API.

## Requirements
- Java
- Maven
- Docker
- RabbitMQ
- MySql

## Developing application locally
Create and run MySql Db and Rabbit Queue:
``` 
$ docker-compose up
```
run server by console with
```
$ mvn spring-boot:run
```

## Test
Test your changes
```
$ mvn test
```
