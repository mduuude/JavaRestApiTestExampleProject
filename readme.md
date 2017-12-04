# Java/Selenium Example Project
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
1. for ALL tests execution
    ```
    mvn test
    ```
2. for UI tests execution
    ```
    mvn test -Dgroups=ui
    ```
3. for API tests execution
    ```
    mvn test -Dgroups=api
    ```

## Structure
* /src/main/java/database - DB utils, specifically, **DBWorker.java** for establishing DB connection
* /src/main/java/model - models for describing DB tables
* /src/main/java/pages - described pages according to page-object pattern

* /src/test/java - test classes itself, respectively **UITest.java**, **APITest.java**