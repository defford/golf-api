# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Project Overview

This is a Spring Boot 3.5.3 application for a Golf API using Java 21. The project uses Maven as the build tool and includes Spring Data JPA with MySQL connector for database operations. It uses Lombok for reducing boilerplate code.

## Build and Development Commands

### Maven Wrapper Setup
The project uses Maven wrapper but requires fixing the wrapper configuration:
```bash
mkdir -p .mvn/wrapper && cp wrapper/maven-wrapper.properties .mvn/wrapper/
```

### Essential Commands
- **Build project**: `./mvnw clean compile`
- **Run application**: `./mvnw spring-boot:run`
- **Run tests**: `./mvnw test`
- **Package application**: `./mvnw clean package`
- **Skip tests during build**: `./mvnw clean package -DskipTests`

### Database Configuration Required
The application currently fails to start because no database configuration is provided. To run successfully, you need to configure MySQL connection properties in `src/main/resources/application.properties`:

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/golf_db
spring.datasource.username=your_username
spring.datasource.password=your_password
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver
spring.jpa.hibernate.ddl-auto=update
```

## Project Structure

- **Main Application**: `src/main/java/com/golfclub/demo/DemoApplication.java`
- **Resources**: `src/main/resources/` (contains application.properties)
- **Tests**: `src/test/java/com/golfclub/demo/`
- **Build Output**: `target/` directory
- **Dependencies**: Defined in `pom.xml`

## Key Dependencies

- Spring Boot Starter Data JPA
- MySQL Connector
- Lombok (with proper annotation processor configuration)
- Spring Boot Test Starter

## Development Notes

- Java 21 is required
- Lombok annotation processing is configured in the Maven compiler plugin
- The application uses standard Spring Boot auto-configuration
- Tests require proper database configuration to pass