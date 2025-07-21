-- Initial database setup for Golf Club API
CREATE DATABASE IF NOT EXISTS golf_db;
USE golf_db;

-- Grant privileges to application user
GRANT ALL PRIVILEGES ON golf_db.* TO 'golfuser'@'%';
FLUSH PRIVILEGES;