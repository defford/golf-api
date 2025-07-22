# Golf API Documentation

## Overview

The Golf API is a Spring Boot application that manages golf club members and tournaments with a many-to-many relationship.

## Base URL

```
http://localhost:8080
```

## API Endpoints

### Members API

#### Get All Members
```
GET /members
```
**Response:** Array of member objects

#### Get Member by ID
```
GET /members/{id}
```
**Parameters:**
- `id` (path): Member ID

#### Create Member
```
POST /members
```
**Request Body:**
```json
{
    "name": "John Doe",
    "address": "123 Main St",
    "email": "john@example.com",
    "phone": "555-0123",
    "startDate": "2024-01-15T10:00:00"
}
```

#### Update Member
```
PUT /members/{id}
```
**Parameters:**
- `id` (path): Member ID
**Request Body:** Member object

#### Delete Member
```
DELETE /members/{id}
```
**Parameters:**
- `id` (path): Member ID

### Tournaments API

#### Get All Tournaments
```
GET /tournaments
```
**Response:** Array of tournament objects

#### Get Tournament by ID
```
GET /tournaments/{id}
```
**Parameters:**
- `id` (path): Tournament ID

#### Create Tournament
```
POST /tournaments
```
**Request Body:**
```json
{
    "name": "Spring Championship",
    "startDate": "2024-04-15T08:00:00",
    "endDate": "2024-04-17T18:00:00",
    "location": "Augusta Golf Course",
    "entryFee": 150.00,
    "cashPrizeAmount": 5000.00
}
```

#### Update Tournament
```
PUT /tournaments/{id}
```
**Parameters:**
- `id` (path): Tournament ID
**Request Body:** Tournament object

#### Delete Tournament
```
DELETE /tournaments/{id}
```
**Parameters:**
- `id` (path): Tournament ID

#### Get Tournaments by Date
```
GET /tournaments/search?startDate=2024-04-15
```
**Parameters:**
- `startDate` (query): Date in YYYY-MM-DD format

### Member-Tournament Relationship

#### Add Member to Tournament
```
POST /members/{memberId}/tournaments/{tournamentId}
```
**Parameters:**
- `memberId` (path): Member ID
- `tournamentId` (path): Tournament ID

#### Remove Member from Tournament
```
DELETE /members/{memberId}/tournaments/{tournamentId}
```
**Parameters:**
- `memberId` (path): Member ID
- `tournamentId` (path): Tournament ID

#### Get Member's Tournaments
```
GET /members/{memberId}/tournaments
```
**Parameters:**
- `memberId` (path): Member ID

#### Get Tournament Participants
```
GET /tournaments/{tournamentId}/members
```
**Parameters:**
- `tournamentId` (path): Tournament ID

## Data Models

### Member
```json
{
    "id": 1,
    "name": "John Doe",
    "address": "123 Main St",
    "email": "john@example.com",
    "phone": "555-0123",
    "startDate": "2024-01-15T10:00:00",
    "tournaments": []
}
```

### Tournament
```json
{
    "id": 1,
    "name": "Spring Championship",
    "startDate": "2024-04-15T08:00:00",
    "endDate": "2024-04-17T18:00:00",
    "location": "Augusta Golf Course",
    "entryFee": 150.00,
    "cashPrizeAmount": 5000.00,
    "members": []
}
```

## Error Responses

### 404 Not Found
```json
{
    "timestamp": "2024-01-15T10:30:00",
    "status": 404,
    "error": "Not Found",
    "message": "Member not found with id: 1",
    "path": "/members/1"
}
```

### 400 Bad Request
```json
{
    "timestamp": "2024-01-15T10:30:00",
    "status": 400,
    "error": "Bad Request",
    "message": "Validation failed",
    "path": "/members"
}
```

## Testing

Use the provided Postman collection to test all endpoints. The collection includes:
- All CRUD operations for Members and Tournaments
- Relationship management endpoints
- Error scenarios
- Sample data for testing

## Authentication

Currently, the API does not require authentication. All endpoints are publicly accessible.

## Rate Limiting

No rate limiting is currently implemented.

## Versioning

This is version 1 of the Golf API. Future versions will maintain backward compatibility.