# Customer Service Rest API
Exercise from class
Solution by Rob Wing

[Problem details](https://github.com/gSchool/ent-microservice-development/blob/master/units/50-spring-microservice-projects/customer-service-api.md)

## API Reference

### **Create Customer**

NOTE: For testing purposes only.  There would be a Customer microservice where this info would come from.
```
curl --location --request POST 'localhost:8080/api/service/customer' \
--header 'Content-Type: application/json' \
--data-raw '{
	"first":"Rob",
	"last":"Wing",
	"address":"123 My Street",
	"city":"Anytown",
	"state":"US",
	"zip":"88888",
	"telephone":"800-555-1212"
}
```

### Get Customer

NOTE: For testing purposes only.  There would be a Customer microservice where this info would come from.
```
curl --location --request GET 'localhost:8080/api/service/customer/800-555-1212'
```

### Create Service Request
``` 
curl --location --request POST 'localhost:8080/api/service' \
--header 'Content-Type: application/json' \
--data-raw '{
	"telephone": "800-555-1212",
	"problem": "Washer won'\''t wash",
	"problemDetails": "All my clothes are dirty.  Why won'\''t it work."
}'
```

### Get All Service Tickets
```
curl --location --request GET 'localhost:8080/api/service'
```

### Get ONE Service Ticket
```
curl --location --request GET 'localhost:8080/api/service/544'
```

### Assign Technician
```
curl --location --request PUT 'localhost:8080/api/service/544?technician=Johnny%20Fixer'
```

### Add Note To Ticket
``` 
curl --location --request PUT 'localhost:8080/api/service/544/notes' \
--header 'Content-Type: application/json' \
--data-raw '{
	"createdBy": "Johnny Fixer",
	"note": "You need to put the clothes in the washer, add soap, run the washer, then put the clothes in the dryer."
}'
```

### Resolve Ticket
``` 
curl --location --request PATCH 'localhost:8080/api/service/544/resolve' \
--header 'Content-Type: application/json' \
--data-raw '{
	"ticketNumber": 544,
	"resolvedBy": "Johnny Fixer",
	"resolutionNotes": "You need to put the clothes in the washer, add soap, run the washer, then put the clothes in the dryer."
}'
```

### Postman Docks
https://documenter.getpostman.com/view/6945911/SzS7R6P5 
