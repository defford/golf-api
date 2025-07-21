# AWS RDS Deployment Guide

## Prerequisites

- AWS Account with RDS access
- MySQL 8.0 RDS instance created
- Security groups configured for port 3306 access

## Environment Variables

Set the following environment variables for AWS deployment:

```bash
export RDS_DB_URL=jdbc:mysql://your-rds-endpoint:3306/golf_db
export RDS_DB_USERNAME=your_username
export RDS_DB_PASSWORD=your_password
```

## Running with AWS Profile

```bash
./mvnw spring-boot:run -Dspring.profiles.active=aws
```

## RDS Configuration

The `application-aws.properties` file is configured for:
- Environment variable-based connection
- Optimized connection pooling (10 max, 5 min idle)
- MySQL 8.0 dialect compatibility
- Production-ready settings (SQL logging disabled)

## Database Schema

The application will automatically create/update the database schema using `spring.jpa.hibernate.ddl-auto=update`.