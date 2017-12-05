# Java RestApi Example Test Project
Test site content with DB data comparing + test API response example

## Prerequisites
* **JDK >= 1.8.0_131**;
* **Apache Maven >= 3.3.9**

## Installation
Install dependencies: 
```
mvn clean install
```

## Run 
Run scripts:
1. for API tests execution
    ```
    mvn test -Dgroups=api
    ```

## Structure
* /src/main/java/model - models for used entities descriptions

* /src/test/java/helpers - used custom tools for testing purposes
* /src/test/java - test classes itself, respectively **APITest.java**