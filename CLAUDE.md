# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Project Overview

This is a comprehensive Spring Boot 3.5.3 Golf Club Management API using Java 21. The application provides REST endpoints for managing golf club members and tournaments with a many-to-many relationship system. It includes complete testing infrastructure, Docker support, and cloud deployment capabilities.

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

### Database Configuration
The application includes multiple database configurations:

- **Default (MySQL)**: `application.properties` - requires local MySQL setup
- **Docker**: `application-docker.properties` - configured for docker-compose setup
- **AWS**: `application-aws.properties` - configured for AWS RDS deployment
- **Testing**: `application-test.properties` - uses embedded H2 database

### Docker Support
The application includes complete Docker containerization:
```bash
# Build and run with Docker Compose
docker-compose up --build

# Run standalone Docker container
docker build -t golf-api .
docker run -p 8080:8080 golf-api
```

## Project Structure

- **Main Application**: `src/main/java/com/golfclub/GolfClubApplication.java`
- **Members Domain**: `src/main/java/com/golfclub/members/` (Controller, Service, Repository, Model)
- **Tournaments Domain**: `src/main/java/com/golfclub/tournaments/` (Controller, Service, Repository, Model)
- **Resources**: `src/main/resources/` (multiple environment configurations)
- **Tests**: `src/test/java/com/golfclub/` (comprehensive test suites for all layers)
- **Docker**: `Dockerfile`, `docker-compose.yml`, `init-scripts/`
- **CI/CD**: `.github/workflows/ci.yml` (GitHub Actions pipeline)
- **Documentation**: `API_DOCUMENTATION.md`, `AWS_DEPLOYMENT.md`

## Key Dependencies

- **Spring Boot Starter Data JPA** - Database operations and entity management
- **Spring Boot Starter Web** - REST API endpoints
- **MySQL Connector J** - Production database driver
- **H2 Database** - In-memory testing database
- **Lombok** - Code generation for reducing boilerplate
- **Spring Boot Test Starter** - Complete testing framework

## API Endpoints

### Members API (`/api/members`)
- CRUD operations: GET, POST, PUT, DELETE
- Search: by name, membership type, phone, tournament date

### Tournaments API (`/api/tournaments`) 
- CRUD operations: GET, POST, PUT, DELETE
- Member management: add/remove members from tournaments
- Search: by start date, location

## Git Workflow - Trunk-Based Development

**IMPORTANT**: This project now follows trunk-based development. The previous complex branching strategy has been consolidated.

### Current Branch Strategy:
- **`main`** - Production-ready code, always deployable
- **`consolidate/golf-api-complete`** - Current consolidated branch with all features integrated
- **Short-lived feature branches** - Max 2-3 days, merge via PR

### Workflow Rules:
1. Always work on short-lived branches from `main`
2. Create descriptive branch names: `feature/add-validation`, `fix/tournament-bug`
3. Run tests before committing: `./mvnw test`
4. Use environment-specific configs, not branches for different environments
5. Delete branches after successful merge

### Deployment Environments:
- **Local Development**: Use default `application.properties`
- **Docker**: Use `application-docker.properties` via `SPRING_PROFILES_ACTIVE=docker`
- **AWS Production**: Use `application-aws.properties` via `SPRING_PROFILES_ACTIVE=aws`

## Development Notes

- **Java 21** is required
- **Comprehensive test coverage** - Controller, Service, and Repository layers
- **CI/CD pipeline** runs automatically on pushes to `main`
- **Docker deployment** ready with database initialization scripts
- **AWS RDS integration** configured for cloud deployment