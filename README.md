# Task Manager

A Spring Boot REST API project I'm building to learn Spring Boot — covering authentication (JWT), Spring Security, and JPA/MySQL persistence.

## Stack

- Java 21
- Spring Boot 4.1 (Web, Data JPA, Validation, Security, Actuator)
- MySQL
- JWT authentication
- Lombok

## Features

- User registration/login with JWT-based auth
- Task CRUD with status and priority
- Pagination support

## Running locally

1. Create a `.env.properties` file (see `.env.properties.example`) with your DB and JWT config.
2. Run:
   ```bash
   ./mvnw spring-boot:run
   ```
