# Home Test Application

A Spring Boot application that provides two main APIs: **Payment Processing** and **Notification Service**. The application is built with Java 17, Spring Boot 3.5.4, and uses PostgreSQL for data persistence and Redis for caching and distributed locking.

## 🚀 Features

### Payment API
- **Idempotent Payment Processing**: Ensures payment operations are safe from duplicate submissions
- **Distributed Locking**: Uses Redis with Redisson for concurrent request handling
- **Request Caching**: Caches payment responses to prevent duplicate processing
- **Conflict Detection**: Detects when the same idempotency key is used with different requests

### Notification API
- **Multi-channel Notifications**: Supports both email and SMS notifications
- **User Preference Management**: Respects user preferences for notification channels
- **Flexible Content**: Supports custom message content for notifications

## 🛠️ Technology Stack

- **Java**: 17
- **Spring Boot**: 3.5.4
- **Database**: PostgreSQL
- **Cache & Locking**: Redis with Redisson
- **Build Tool**: Gradle
- **Lombok**: For reducing boilerplate code

## 📋 Prerequisites

- Java 17 or higher
- PostgreSQL database
- Redis server
- Gradle (or use the included Gradle wrapper)

## ⚙️ Configuration

Update the `application.yml` file with your database and Redis configurations:

```yaml
spring:
  datasource:
    url: jdbc:postgresql://{host}:{port}/mydatabase
    username: user
    password: password
  
  data:
    redis:
      host: host
      port: port
      password: pass
```

## 🗄️ Database Setup

Run the SQL script in `src/main/resources/data/user.sql` to create the users table and insert sample data:

```sql
CREATE TABLE users (
    id SERIAL PRIMARY KEY,
    email VARCHAR(255),
    phone VARCHAR(255),
    email_enabled BOOLEAN,
    sms_enabled BOOLEAN
);

INSERT INTO users(email, phone, email_enabled, sms_enabled)
VALUES ('user1@example.com', '+123456789', true, false),
       ('user2@example.com', '+987654321', false, true);
```

## 🚀 Running the Application

1. **Build the project**:
   ```bash
   ./gradlew build
   ```

2. **Run the application**:
   ```bash
   ./gradlew bootRun
   ```

The application will start on port 8080.

## 📚 API Documentation

### 1. Payment API

#### Endpoint: `POST /api/payments`

Processes payment requests with idempotency support.

**Headers:**
- `Idempotency-Key` (required): Unique identifier for the payment request

**Request Body:**
```json
{
  "accountId": "string",
  "amount": 100.00,
  "currency": "USD",
  "message": "Payment for services"
}
```

**Response:**
```json
{
  "transactionId": "uuid-string",
  "status": "SUCCESS",
  "message": "Payment processed successfully"
}
```

**Features:**
- **Idempotency**: Same request with same idempotency key returns cached response
- **Concurrent Safety**: Uses Redis distributed locks to handle concurrent requests
- **Conflict Detection**: Returns 409 Conflict if idempotency key is reused with different request
- **Processing Status**: Returns 202 Accepted if request is being processed

**Status Codes:**
- `200 OK`: Payment processed successfully
- `202 Accepted`: Payment is being processed
- `409 Conflict`: Idempotency key conflict
- `500 Internal Server Error`: Processing error

### 2. Notification API

#### Endpoint: `POST /notifications/send/{userId}?message={message}`

Sends notifications to users via their preferred channels (email and/or SMS).

**Path Parameters:**
- `userId` (required): User ID to send notification to

**Query Parameters:**
- `message` (required): Notification message content

**Example Request:**
```
POST /notifications/send/1?message=Your payment has been processed successfully
```

**Response:**
- `200 OK`: Notification sent successfully
- `500 Internal Server Error`: User not found or invalid contact information

**Features:**
- **User Preference Support**: Only sends to enabled channels (email/sms)
- **Contact Validation**: Validates email and phone number availability
- **Multi-channel**: Can send to both email and SMS if both are enabled
- **Error Handling**: Throws exceptions for invalid users or missing contact info

## 🔧 Implementation Details

### Payment Processing Flow

1. **Idempotency Check**: Validates if request with same key was already processed
2. **Lock Acquisition**: Uses Redis distributed lock to prevent concurrent processing
3. **Double-check**: Re-validates cache after acquiring lock
4. **Payment Processing**: Simulates external payment service (10-second delay)
5. **Response Caching**: Stores successful response in Redis with 10-minute TTL

### Notification Processing Flow

1. **User Lookup**: Retrieves user from database by ID
2. **Channel Validation**: Checks if email/SMS channels are enabled
3. **Contact Validation**: Ensures email/phone numbers are available
4. **Multi-channel Delivery**: Sends to all enabled channels

### Key Components

- **RedisIdempotency**: Handles idempotency caching and distributed locking
- **EmailUtil/SmsUtil**: Placeholder implementations for notification delivery
- **UserRepository**: JPA repository for user data access
- **PaymentService/NotificationService**: Business logic interfaces and implementations

## 🧪 Testing

Run the tests using:
```bash
./gradlew test
```

## 📝 Notes

- The payment processing includes a 10-second delay to simulate external API calls
- Email and SMS utilities are placeholder implementations (logging only)
- Redis cache has a 10-minute TTL for payment responses
- The application uses Spring's transaction management for data consistency

## 🔒 Security Considerations

- Idempotency keys should be unique and unpredictable
- Consider implementing rate limiting for production use
- Add authentication and authorization for production deployment
- Validate and sanitize all input parameters
