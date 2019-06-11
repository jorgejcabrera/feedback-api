# feedback-api API Rest

## Overview
API Rest for feedback-api.

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
