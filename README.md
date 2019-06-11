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

## Examples
Leaving feedback for purchase
```
curl -X POST 
  localhost:8080/feedback-api 
  -H 'content-type: application/json'
  -d '{
	"order_id": 13,
	"seller_id": 1,
	"buyer_id": 2,
	"item_id": 1,
	"comment": "It is a good product",
	"store_id": "AR4",
	"score": "DIAMOND"
   }'
```
Get order feedback 
```
curl -X GET localhost:8080/feedback-api/order/13
```
Get user feedback list
```
curl -X GET \
    localhost:8080/feedback-api/user/1?from=2018-02-02&to=2019-12-12&page=0&size=2
```
Get store feedback list
```
curl -X GET \
    localhost:8080/feedback-api/store/AR4?from=2018-02-02&to=2019-12-12&page=0&size=2
```