# Customer Service Rest API
Exercise from class
Solution by Rob Wing

- [Exercise details](https://github.com/gSchool/ent-microservice-development/blob/master/units/50-spring-microservice-projects/customer-service-api.md)
- [API Reference](https://documenter.getpostman.com/view/6945911/SzS7R6P5) 

### Acceptance Criteria
Build a Customer Service application, where customers can request help for an issue or question.  The data must be persisted
in a relational database and have the following end points...
- create new request
- get all requests
- get one request by id
- assign a request to a technician
- update a request with a note 
- resolve a request

### My Development Process

1. Create known entities with known attributes
    - Customer
    - ServiceTicket
    - ServiceNote
1. Create JPA repositories
    - CustomerRepo
    - ServiceTicketRepo
    - ServiceNoteRepo
1. Start the app to generate your tables.
1. Create Service Layer
    - Create CustSvcService class
    - Generate Test Class from Service
    - Build tests for known requirements and implement ***one at a time***
1. Create Controller
    - Create ServiceController class
    - Generate Test Class from controller
    - Build tests for known requirements and implement ***one at a time***
    
Note:  You can start with the controller and build through to your service if you wish.  This technique 
may give you better insight to how your service should be.  My prefrence, is to 
write the service first, but I have done it both ways.  Starting with your controller gives you the full view
into your application.