The Spring Boot application uses MVC and REST controllers: 
- Thymeleaf templates are used for the Admin and Doctor dashboards;
- REST APIs serve all the other modules. 

In addition, the application lies on two databases: 
- MySQL for patient, doctor, appointment, and admin structured data, using JPA entities
- MongoDB for prescriptions using flexible document models. 

All the controllers make requests to a common service layer, which delegates to the appropriate repository.


The application is composed by 7 layers:
1. User accesses through web dashboard or API.
2. The action is routed to the Thymeleaf controllers for web dashboard or REST controller for API.
3. The controller calls the service layer, which acts as the heart of the backend system.
4. The service layer communicates with the repository layer, i.e. with MySQL or MongoDB repositories.
5. Each repository interfaces directly with the underlying MySQL or MongoDB database.
6. The data retrieved from the database is mapped into Java model classes using model binding: MySQL data is converted into JPA entities, MongoDB data is loaded into document objects.
7. The bound models are used in the response layer: MVC flows for the web dashboard, REST flows to serialize the model into JSON and pass it with the HTTP response.