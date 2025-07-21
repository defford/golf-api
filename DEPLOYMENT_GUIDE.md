# Golf API - Dual Deployment Guide

This Golf Club Management API supports **two distinct deployment scenarios** using Spring Profiles for automatic configuration switching.

## 🎓 **For Professors/Evaluators - Local Docker Deployment**

**Recommended for: Course evaluation, local testing, development**

### Quick Start (Zero Configuration Required)
```bash
git clone <repository-url>
cd golf-api
docker-compose up --build
```

**That's it!** The API will be available at `http://localhost:8080`

### What This Does
- ✅ Uses `SPRING_PROFILES_ACTIVE=docker` profile automatically
- ✅ Spins up local MySQL 8.0 container
- ✅ Creates `golf_db` database with user `golfuser`
- ✅ Automatically initializes database schema
- ✅ No external dependencies or configuration needed

### Testing the API
Use the included Postman collection: `Golf_API_Postman_Collection.json`
Or access endpoints directly:
- GET `http://localhost:8080/api/members` - View all members
- GET `http://localhost:8080/api/tournaments` - View all tournaments
- See `API_DOCUMENTATION.md` for complete endpoint list

### Stopping the Application
```bash
docker-compose down
```

---

## ☁️ **For AWS RDS Production Deployment**

**Recommended for: Production use, cloud deployment, scalable environments**

### Prerequisites
1. **AWS RDS MySQL 8.0 Instance** - Created and accessible
2. **Security Groups** - Configured to allow port 3306 access
3. **Database Created** - Database named `golf_db` in your RDS instance

### Step 1: Set Environment Variables
```bash
export RDS_DB_URL="jdbc:mysql://your-rds-endpoint.region.rds.amazonaws.com:3306/golf_db"
export RDS_DB_USERNAME="your_username"
export RDS_DB_PASSWORD="your_password"
```

### Step 2: Deploy Application
```bash
# Build the application
./mvnw clean package -DskipTests

# Run with AWS profile
java -Dspring.profiles.active=aws -jar target/demo-0.0.1-SNAPSHOT.jar
```

### Step 3: Verify Deployment
- Application starts on port 8080
- Connects to AWS RDS automatically
- Creates/updates database schema automatically
- API endpoints available at your server's IP:8080

### AWS Configuration Details
The `application-aws.properties` includes:
- Environment variable-based connection strings
- Optimized HikariCP connection pooling (max 10, min idle 5)
- Production-ready settings (SQL logging disabled)
- MySQL 8.0 dialect compatibility

---

## 🔧 **Configuration Profiles Explained**

| Profile | Use Case | Database | Port | Configuration File |
|---------|----------|----------|------|-------------------|
| `docker` | Professor evaluation, local Docker | Local MySQL container | 8080 | `application-docker.properties` |
| `aws` | Production AWS deployment | AWS RDS | 8080 | `application-aws.properties` |
| `default` | Local development | Local MySQL (`localhost:3306`) | 8080 | `application.properties` |
| `test` | Unit/Integration testing | H2 in-memory | N/A | `application-test.properties` |

---

## 🛠 **Advanced Usage**

### Manual Database Setup (Optional)
For AWS deployment, you can manually create the database:
```sql
CREATE DATABASE golf_db;
CREATE USER 'golfuser'@'%' IDENTIFIED BY 'your_password';
GRANT ALL PRIVILEGES ON golf_db.* TO 'golfuser'@'%';
FLUSH PRIVILEGES;
```

### Custom Port Configuration
```bash
# Docker deployment on custom port
docker-compose up --build -d
docker-compose exec golf-api java -Dserver.port=9090 -jar /app/target/demo-0.0.1-SNAPSHOT.jar

# AWS deployment on custom port
java -Dspring.profiles.active=aws -Dserver.port=9090 -jar target/demo-0.0.1-SNAPSHOT.jar
```

### Health Check Endpoints
- `GET /api/members` - Basic functionality test
- `GET /api/tournaments` - Verify database connectivity

---

## 🚨 **Troubleshooting**

### Docker Issues
```bash
# Clean rebuild if issues occur
docker-compose down -v
docker-compose up --build
```

### AWS RDS Connection Issues
1. **Check Security Groups** - Ensure port 3306 is accessible
2. **Verify Environment Variables** - Check RDS_DB_URL format
3. **Test Connection** - Use MySQL client to test RDS connectivity
4. **Check Logs** - Application logs show detailed connection errors

### Port Already in Use
```bash
# Find process using port 8080
lsof -i :8080
# Kill process if needed
kill -9 <PID>
```

---

## 📚 **Additional Resources**

- **API Documentation**: `API_DOCUMENTATION.md`
- **AWS Specific Setup**: `AWS_DEPLOYMENT.md`
- **Postman Collection**: `Golf_API_Postman_Collection.json`
- **Test Examples**: `src/test/` directory

---

## 🎯 **Summary**

This project is designed to work seamlessly in both environments:

- **Professors get**: One-command Docker deployment with zero configuration
- **Production gets**: Scalable AWS RDS deployment with optimized settings
- **Same codebase**: Automatic environment detection and configuration switching

Both deployments use the identical API endpoints and functionality - the only difference is the underlying database infrastructure.