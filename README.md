# Native JPA blocking example
This is a [Person-Rest-API](http://localhost:8081/swagger-ui/index.html) example that provides:
* `GET /api/v1/person`: find all person with pagination & sorting
* `GET /api/v1/person/{id}`: find person by id
* `GET /api/v1/person/stream`: A SSE (Server Sent Event) Stream that notify each Person's Event (Create, Update, Delete)
* `POST /api/v1/person`: create a new person with validations
* `PUT /api/v1/person`: update an existing person with validations
* `DELETE /api/v1/person/{id}`: delete an existing person by id 

Following best practices like:
* SOLID
    * [Single responsibility principle](https://en.wikipedia.org/wiki/Single-responsibility_principle): Every module, class or function should have responsibility over a single part of that program's functionality, and it should encapsulate that part. Spring plays a huge role here providing annotations like `@Repository @Service @RestController` in order to tag/label each role for each class.
    * [Open-closed principle](https://en.wikipedia.org/wiki/Open%E2%80%93closed_principle): An entity can allow its behaviour to be extended without modifying its source code. For example the [PersonEventLister](src/main/java/com/z/nativejpablocking/person/service/PersonEventListener.java) will listen to every new event that `extends` from `PersonEvent`.
    * [Liskov substitution principle](https://en.wikipedia.org/wiki/Liskov_substitution_principle): if S is a subtype of T, then objects of type T may be replaced with objects of type S without altering any of the desirable properties of the program.
    * [Interface segregation principle](https://en.wikipedia.org/wiki/Interface_segregation_principle): Splits interfaces that are very large into smaller and more specific ones so that clients will only have to know about the methods that are of interest to them. For example the [PersonManagementServiceImpl](src/main/java/com/z/nativejpablocking/person/service/PersonManagementServiceImpl.java) `implements` both `PersonManagementService` and `SseEmitterService`.
    * [Dependency inversion principle](https://en.wikipedia.org/wiki/Dependency_inversion_principle): relationships established from high-level, policy-setting modules to low-level, dependency modules are reversed, thus rendering high-level modules independent of the low-level module implementation details. Example [PersonController](src/main/java/com/z/nativejpablocking/person/controller/PersonController.java) doesn't have any dependency with `PersonDAO` instead depends on `PersonManagementService`. 
* Layers division 
    * **Controller**: Part of the [Model View Controller](https://en.wikipedia.org/wiki/Model%E2%80%93view%E2%80%93controller) design pattern that will handle http request and route them into the Service layer.
    * **Service**: The [Service Layer](https://martinfowler.com/eaaCatalog/serviceLayer.html) 
      defines an application's boundary with a layer of services that establishes a set of available 
      operations and coordinates the application's response in each operation.
    * **DTO**: [Data Transfer Object](https://martinfowler.com/eaaCatalog/dataTransferObject.html) an object that carries data between processes.
    * **DAO**: [Data Access Object](https://en.wikipedia.org/wiki/Data_access_object) is a structural pattern that allows us to isolate the application/business layer from the persistence layer using an abstract API. 
    * **Domain**: [Object models](https://martinfowler.com/eaaCatalog/domainModel.html) of the domain that incorporates both behavior and data.
* Never expose Domain Objects over the public API      
* Code coverage >= 80%
#Database ER Model
![Database ER model](database%20ER%20model.png "Database ER model")

#Dependencies
* `Java11`
* `Maven 3.6.3`
* `Spring Boot v2.4.4 - Spring v5.3.5`: Web blocking "Servlet" (Due JDBC)
* `Spring Data JPA`: With EntityGraph (to avoid [n+1 problem](https://stackoverflow.com/questions/97197/what-is-the-n1-selects-problem-in-orm-object-relational-mapping))
* `Hibernate v5.4.29Final`: ORM (Object Relational Mapping) library
* `H2 v1.4`: in memory database
* `Flyway v7.1.1`: database migration manager
* `Swagger v3.0.0`
* `Junit v5`

#How to run
`make run` or `mvn spring-boot:run`

#Project structure
* `main`
    * `java`
        * `configuration`: Application configurations like `Swagger`
        * `error`: Error handling layer using `@ControllerAdvice` spring feature.
        * `person` All the following layers related with `Person` Entity
            * `controller`
            * `dao`
            * `domain`
            * `dto`
            * `service`
        * `job`:  same as `Person`
        * `utils`: Utility classes or functions
    * `resources`
        * `application.properties`: Default application properties
        * `application-test.properties`: Application properties for test profile.
        * `db.migration`: All the database scripts migrations in `flyway` filename nomenclature.
    * `test`
        * `java`
            * `person.controller`: Integration Test for Person Controller Layer
            * `utils`: Utility classes or functions for Tests
        * `resources`
            * `data.sql`: Insert sql script to populate the database before run the tests (Automatically)