Subscriptions: A Spring Boot REST API example
==

Building
--

The project is built by running:

    mvn install
    
The `install` phase also generates a `docker` image under the name:

    diogosantos/subscriptions-api    

<a name="running"></a> Running
--

To run the service, after building the project, at the project folder, execute the following command:

    java -jar target/subscriptions-api-0.0.1.jar
    
The server will start and listen for requests on the port `8080` and under the context:

    /api

For example:

    http://localhost:8080/api

Alternatively, it's also possible to run the service as a container. For doing so, execute the following command:

    docker run -p 8080:8080 -t diogosantos/subscriptions-api:latest        
      
Service Status and Metrics
--
While the services is running, it's possible to monitor its status through the following endpoint:

    http://localhost:8080/api/status
    

Security
--
The endpoint for Service Status and Metrics is protected by Basic Http Authentication. The credentials are as follows:

    Username: admin
    Password: admin

All the other endpoints of the application are publicly available.

Circuit Breaker
--
In order to test the circuit breaker with the in memory database, do the following:

1. Configure the service to connect to an external H2 server by adding the following property to the `application.properties` file:

        spring.datasource.url = jdbc:h2:tcp://localhost/~/subscriptions
    
2. Initiate an instance of the H2 database as a standalone server:
    
        java -cp h2-1.4.197.jar org.h2.tools.Server -tcp
        
    it's good to pay attention to the H2 jar name. The version may be a bit different, but it should work the same.
        
3. Run the service, accordingly with the [Running](#running) section.

4. After the application finishes its start up, kill the standalone instance of the H2 database.

5. Enjoy the application with fall backs for the endpoint calls :)            

