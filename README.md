# Gateway Manager UI and backend
This is a sample application based on Spring Boot 2.4 backend and Angular 11 front end.


## Prerequisites

- [x] JDK 8 or later 
- [x] Apache Maven 3.5 or later 
- [x] Node.js (v14.16.0 or compatible)
- [x] Angular CLI (v11 or compatible)
- [x] MySQL (v5.7 or compatible)
- [x] git client


## how to run this application

clone this repository using git command or alternatively , you may be able to download ZIP archive and extract it

```bash
git clone https://github.com/melanj/gateway-manager.git
```


Update src/main/resources/application.yml file according to MySQL server hostname, database and user credentials
```yml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/app?serverTimezone=UTC
    username: root
    password: root
  dbcp2:
    max-wait-millis: 30000
    validation-query: "SELECT 1"
    validation-query-timeout: 30
  jpa:
    hibernate:
      ddl-auto: update
    open-in-view: false
    properties:
      hibernate:
        dialect: org.hibernate.dialect.MySQL5InnoDBDialect
        jdbc:
          lob:
            non_contextual_creation: true
        id:
          new_generator_mappings: true
springdoc:
  pathsToMatch: /api/**
```
