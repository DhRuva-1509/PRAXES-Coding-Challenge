# Telemedicine Consultation Messages - Backend
A RESTful API system for storing and retrieving messages from telemedicine consultations between patients and doctors.

## Core User Story

"As a doctor or patient, I need to view all messages exchanged during my virtual consultation so I can review our discussion."
***

## Table of Contents

- [Quick Start](#quick-start)
- [API Documentation](#api-documentation)
- [Architecture Decisions](#architecture-decisions)
- [Making This Production-Ready](#making-this-production-ready)
- [Testing the Application](#testing-the-application)

***
## Quick Start

### Prerequisites

- **Docker Desktop** installed and running
- No services running on ports `8080` (application) or `5433` (PostgreSQL)

### Running the Application

```bash
  # Clone the repository
  git clone https://github.com/DhRuva-1509/PRAXES-Coding-Challenge.git
  cd backend

  # Start the application with Docker Compose
  docker-compose up --build
```

This single command will:
- Build the Spring Boot application
- Start a PostgreSQL 16 database
- Create the necessary tables
- Load sample seed data (2 consultations with 14 messages)
- Start the application on http://localhost:8080

### Stopping the Application
```bash
  # Stop and keep data
  docker-compose down

  # Stop and remove all data
  docker-compose down -v
```

## API Documentation
### Base URL
```bash
  http://localhost:8080/api
```
### Response Format
All responses follow this consistent structure:

```json
{
  "statusCode": 200,
  "body": { /* … */ },
  "url": "/api/…",
  "timestamp": "2025-10-19T22:50:00"
}
```

### 1. Retrieve All Messages for a Consultation
Endpoint: ```GET /consultations/{consultationId}/messages```

Description: Returns all messages for a specific consultation in chronological order.

Example Request:
``` bash

curl http://localhost:8080/api/consultations/1/messages
```

Example Response (200 OK):
```json
{
  "statusCode": 200,
  "body": [
    {
      "id": 1,
      "consultationId": 1,
      "author": "P123",
      "authorRole": "PATIENT",
      "content": "Hello Doctor, I have been experiencing severe headaches for the past week.",
      "timestamp": "2025-10-19T09:05:00"
    },
    {
      "id": 2,
      "consultationId": 1,
      "author": "D456",
      "authorRole": "DOCTOR",
      "content": "Hello! I am sorry to hear that. Can you describe the type of pain you are experiencing?",
      "timestamp": "2025-10-19T09:07:00"
    }
    // ... more messages in chronological order
  ],
  "url": "/api/consultations/1/messages",
  "timestamp": "2025-10-19T22:50:00"
}
```

### 2. Filter Messages by Author Role
Endpoint: ```GET /consultations/{consultationId}/messages?role={PATIENT|DOCTOR}```

Description: Returns only messages from the specified role (PATIENT or DOCTOR).

Query Parameters:
- ```role (optional)```: Filter by PATIENT or DOCTOR

Example Request (Doctor messages only):
``` bash

curl "http://localhost:8080/api/consultations/1/messages?role=DOCTOR"
```

Example Response (200 OK):
```json
{
  "statusCode": 200,
  "body": [
    {
      "id": 2,
      "consultationId": 1,
      "author": "D456",
      "authorRole": "DOCTOR",
      "content": "Hello! I am sorry to hear that...",
      "timestamp": "2025-10-19T09:07:00"
    },
    {
      "id": 4,
      "consultationId": 1,
      "author": "D456",
      "authorRole": "DOCTOR",
      "content": "Have you experienced any visual disturbances...",
      "timestamp": "2025-10-19T09:12:00"
    }
  ],
  "url": "/api/consultations/1/messages",
  "timestamp": "2025-10-19T22:50:00"
}
```

Example Request (Patient messages only):
``` bash

curl "http://localhost:8080/api/consultations/1/messages?role=PATIENT"
```
```json
{
  "statusCode": 200,
  "body": [
    {
      "id": 1,
      "consultationId": 1,
      "author": "P123",
      "authorRole": "PATIENT",
      "content": "Hello Doctor, I have been experiencing severe headaches for the past week.",
      "timestamp": "2025-10-19T09:05:00"
    },
    {
      "id": 3,
      "consultationId": 1,
      "author": "P123",
      "authorRole": "PATIENT",
      "content": "It feels like a throbbing pain on the right side of my head.",
      "timestamp": "2025-10-19T09:10:00"
    }
  ],
  "url": "/api/consultations/1/messages",
  "timestamp": "2025-10-20T04:10:34"
}
```
### 3. Add a New Message to a Consultation
Endpoint: ```POST /consultations/{consultationId}/messages```

Description: Adds a new message to an existing consultation.

Request Body:
``` json
{
  "author": "P123",
  "authorRole": "PATIENT",
  "content": "Thank you Doctor, I will follow your advice!"
}
```
Required Fields:
- ```author``` (string): User ID who sent the message
- ```authorRole``` (string): Must be either PATIENT or DOCTOR
- ```content``` (string): The message text

Note: ```consultationId``` is provided in the URL path, and ```timestamp``` is generated automatically by the server.

Example Request:
```bash

curl -X POST http://localhost:8080/api/consultations/1/messages \
  -H "Content-Type: application/json" \
  -d '{
    "author": "P123",
    "authorRole": "PATIENT",
    "content": "Thank you Doctor, I will follow your advice!"
  }'
```

Example Response:
```json
{
  "statusCode": 201,
  "body": {
    "id": 15,
    "consultationId": 1,
    "author": "P123",
    "authorRole": "PATIENT",
    "content": "Thank you Doctor, I will follow your advice!",
    "timestamp": "2025-10-19T22:51:30"
  },
  "url": "/api/consultations/1/messages",
  "timestamp": "2025-10-19T22:51:30"
}
```

### 4. Create a New Consultation (Created just to add new consultation id and create messages for it)
Endpoint: ```POST /consultations```

Description: Creates a new consultation between a patient and doctor.

Request Body:
``` json
{
  "patientId": "P999",
  "doctorId": "D888"
}
```

Example Request:
```bash

curl -X POST http://localhost:8080/api/consultations \
  -H "Content-Type: application/json" \
  -d '{
    "patientId": "P999",
    "doctorId": "D888"
  }'
```

Example Response:
```json
{
  "statusCode": 201,
  "body": {
    "id": 3,
    "patientId": "P999",
    "doctorId": "D888",
    "createdAt": "2025-10-19T22:52:00",
    "messages": []
  },
  "url": "/api/consultations",
  "timestamp": "2025-10-19T22:52:00"
}
```

### 5. Get a Specific Consultation
Endpoint: ```GET /consultations/{id}```

Example Request:
```bash

curl http://localhost:8080/api/consultations/1
```

Example Response:
```json
{
  "statusCode": 200,
  "body": {
    "id": 1,
    "patientId": "P123",
    "doctorId": "D456",
    "createdAt": "2025-10-19T09:00:00",
    "messages": []
  },
  "url": "/api/consultations/1",
  "timestamp": "2025-10-19T22:53:00"
}
```
### Error Responses
1. 404 - Consultation Not Found

```bash

curl http://localhost:8080/api/consultations/999/messages
```

```json
{
  "statusCode": 404,
  "body": "Consultation not found with id: 999",
  "url": "/api/consultations/999/messages",
  "timestamp": "2025-10-19T22:54:00"
}
```

2. 400 - Invalid Author Role
```bash
 curl -X POST http://localhost:8080/api/consultations/1/messages \
  -H "Content-Type: application/json" \
  -d '{
    "author": "P123",
    "authorRole": "NURSE",
    "content": "Test message"
  }'
```

```json
{
  "statusCode": 400,
  "body": "Author role must be either PATIENT or DOCTOR",
  "url": "/api/consultations/1/messages",
  "timestamp": "2025-10-19T22:56:00"
}
```
3. 400 - Validation Error (Missing Required Fields)
```bash
 curl -X POST http://localhost:8080/api/consultations/1/messages \
  -H "Content-Type: application/json" \
  -d '{
    "author": "P123"
  }'
```
```json
{
  "statusCode": 400,
  "body": "Author role is required",
  "url": "/api/consultations/1/messages",
  "timestamp": "2025-10-19T22:55:00"
}
```

### Sample Data
The application comes pre-loaded with seed

#### Consultation 1: Patient P123 with Doctor D456

#### Topic: Headaches and migraines
Messages (6 total):
- 09:05 - P123 (PATIENT): "Hello Doctor, I have been experiencing severe headaches..."
- 09:07 - D456 (DOCTOR): "Hello! I am sorry to hear that. Can you describe the pain..."
- 09:10 - P123 (PATIENT): "It feels like a throbbing pain on the right side..."
- 09:12 - D456 (DOCTOR): "Have you experienced any visual disturbances..."
- 09:15 - P123 (PATIENT): "Yes, bright lights make it worse..."
- 09:20 - D456 (DOCTOR): "This sounds like migraines. I will prescribe medication..."


#### Consultation 2: Patient P789 with Doctor D012

#### Topic: Medication side effects
Messages (8 total):
- 10:05 - P789 (PATIENT): "Hi Doctor, I wanted to discuss some side effects..."
- 10:08 - D012 (DOCTOR): "Of course. What symptoms have you been experiencing?"
- 10:12 - P789 (PATIENT): "I have been feeling quite nauseous..."
- 10:15 - D012 (DOCTOR): "That is a known side effect. Have you been taking it with food?"
- 10:18 - P789 (PATIENT): "I have been taking it on an empty stomach..."
- 10:22 - D012 (DOCTOR): "Yes, definitely take it with food..."
- 10:25 - P789 (PATIENT): "Thank you! Should I continue with the same dosage?"
- 10:28 - D012 (DOCTOR): "Yes, continue with the same dosage but with food..."

## Architecture Decisions

### Data Model
#### Design Overview
I designed a one-to-many relationship between consultations and messages:
```scss
Consultation (1) ----> Messages (Many)
```
Each consultation can have multiple messages, but each message belongs to exactly one consultation.
#### Entity: Consultation
```java
@Entity
@Table(name = "consultations")
public class Consultation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "patient_id", nullable = false)
    private String patientId;

    @Column(name = "doctor_id", nullable = false)
    private String doctorId;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @OneToMany(mappedBy = "consultation", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<Message> messages = new ArrayList<>();
}
```

Fields Included:
- ```id``` (Long, Primary Key): Auto-generated unique identifier.
- ```patientId``` (String): Identifier for the patient in the consultation.
- ```doctorId``` (String): Identifier for the doctor in the consultation.
- ```createdAt``` (LocalDateTime): When the consultation started.

Why these fields?
- ```patientId``` and ```doctorId``` uniquely identify the participants.
- ```createdAt``` provides an audit trail and allows sorting consultations by date.
- In a real system, these IDs would be foreign keys to User tables.

#### Entity: Message
```java
@Entity
@Table(name = "messages")
public class Message {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "consultation_id", nullable = false)
    @JsonBackReference
    private Consultation consultation;

    @Column(name = "author", nullable = false)
    private String author;

    @Column(name = "author_role", nullable = false)
    private String authorRole;

    @Column(name = "content", columnDefinition = "TEXT", nullable = false)
    private String content;

    @Column(name = "timestamp", nullable = false)
    private LocalDateTime timestamp;
}
```

Fields Included:
- ```id``` (Long, Primary Key): Auto-generated unique identifier.
- ```consultation_id``` (Long, Foreign Key): Links to parent consultation.
- ```author``` (String): User ID of the message sender
- ```authorRole``` (String): Either "PATIENT" or "DOCTOR" - validated at API level.
- ```content``` (Text): The actual message content.
- ```timestamp``` (LocalDateTime): Server-generated timestamp for message ordering.

Why these fields?
- ```author``` and ```authorRole``` enable filtering and proper attribution
- ```content``` stored as TEXT type for longer messages
- ```timestamp``` ensures chronological ordering (server-side to prevent manipulation)
- Foreign key relationship ensures referential integrity

#### Database Indexes
For production performance, I would index:
```sql
-- Primary lookups
CREATE INDEX idx_messages_consultation_id ON messages(consultation_id);

-- Filtering by role
CREATE INDEX idx_messages_author_role ON messages(author_role);

-- Chronological ordering
CREATE INDEX idx_messages_timestamp ON messages(timestamp);

-- Composite index for common query pattern
CREATE INDEX idx_messages_consultation_role_timestamp 
ON messages(consultation_id, author_role, timestamp);

-- User lookups
CREATE INDEX idx_consultations_patient_id ON consultations(patient_id);
CREATE INDEX idx_consultations_doctor_id ON consultations(doctor_id);
```
Why these indexes?
- ```consultation_id``` - Most common lookup pattern (get all messages for a consultation).
- ```author_role``` - Enables fast filtering by PATIENT/DOCTOR.
- ```timestamp``` - Ensures efficient chronological sorting.
- Composite index - Optimizes the most common query: filtered messages in order.
- Patient/Doctor IDs - For user-specific consultation lists.

### Technology Choices
#### 1. SpringBoot:
- Mature ecosystem - Production-ready with extensive healthcare integrations.
- Type safety - Compile-time error checking reduces bugs.
- Strong ORM - Hibernate/JPA handles complex relationships elegantly.

#### 2. PostgreSQL:
- ACID compliance - Critical for healthcare data integrity.
- JSON support - Future extensibility for message metadata.
- HIPAA-compliant - Can be configured for healthcare compliance.
- Robust indexing - Excellent performance for time-series message data.

#### 3. Docker + Docker Compose::
- Reproducible environment - "Works on my machine" problem solved.
- Easy setup - Single command to run entire stack.
- Production-similar - Same container approach used in production.
- Isolated dependencies - No conflicts with local PostgreSQL.

### Architecture Pattern: Layered Architecture
```scss
Controller Layer (REST API)
          ↓
Service Layer (Business Logic)
          ↓
Repository Layer (Data Access)
          ↓
Database (PostgreSQL)
```

Why this pattern?
- Separation of concerns - Each layer has a single responsibility.
- Testability - Can mock service layer for controller tests.
- Maintainability - Business logic isolated from HTTP/database concerns.
- Standard pattern - Familiar to any Spring Boot developer.

### Trade-offs Made
#### 1. Time vs. Feature Completeness:
- Chose: Core functionality (add/retrieve/filter messages).
- Skipped: Authentication, comprehensive tests, deployment scripts.
- Reasoning: Per instructions, focus on working solution and documentation.
#### 2. Data Model Simplicity:
- Chose: String IDs for patient/doctor (e.g., "P123", "D456").
- Skipped: Full User entity with relationships.
- Reasoning: Time constraint; in production would have proper User tables.

#### 3. Validation:
- Chose: Jakarta Validation annotations (@NotBlank, @Pattern).
-  Skipped: Custom validators for business rules.
- Reasoning: Adequate for MVP.

#### 4. Error Handling:
- Chose: Global exception handler with consistent response format.
- Skipped: Detailed error codes and internationalization.
- Reasoning: Demonstrates pattern without over-engineering.

## Making This Production-Ready
### Security
Current State: No authentication or authorization

#### Production Improvements:

#### 1. Authentication & Authorization
- Implement OAuth 2.0 / JWT tokens for user authentication.
- Role-based access control (RBAC) for Patients, Doctors and Admin.

#### 2. Data Encryption
- Encrypt sensitive message content at rest (AES-256).
- Use TLS 1.3 for all API communications.
- Encrypt PostgreSQL connections with SSL.

#### 3. HIPAA Compliance
- Implement audit logging for all data access (who, what, when).
- Add data retention policies (e.g., 7-year retention for medical records).
- Enable PostgreSQL row-level security.
- Implement automatic PHI (Protected Health Information) detection.

#### 4. API Security
- Add rate limiting (e.g., 100 requests/minute per user).
- Implement CORS policies with whitelist.
- Add request validation middleware.
- Use API keys for service-to-service communication.

#### 5. Input Sanitization
- Sanitize HTML/SQL injection attempts.
- Validate all user inputs server-side.
- Implement content-length limits (prevent DoS).


### Performance
Current State: Basic queries with no optimization.

#### Production Improvements:
#### 1. Caching Strategy
- Implement Redis cache for frequently accessed consultations.
- Cache consultation metadata (patient/doctor info).
- Time-based cache invalidation (15-minute TTL for messages).

#### 2. Database Optimization
- Add database indexes (as documented above).
- Implement connection pooling (HikariCP with 20-50 connections).
- Enable query result pagination (default 50 messages per page).
- Add database read replicas for high-traffic reads.

#### 3. Query Optimization
- Implement lazy loading for message relationships
- Add database query explain plans and monitoring
- Use projection DTOs to return only needed fields

#### 4. API Response Optimization
- Compress responses with GZIP.
- Implement ETag caching for unchanged data.
- Add pagination headers (Link, X-Total-Count).

### Reliability
Current State: Single instance, no monitoring.

#### Production Improvements:
#### 1. Health Checks
- Add Spring Boot Actuator endpoints (/health, /metrics).
- Implement database connection health checks.
- Add readiness and liveness probes for Kubernetes.

#### 2. Monitoring & Alerting
- Integrate with Prometheus + Grafana for metrics.
- Add application performance monitoring (APM) like New Relic.
- Set up alerts for:
- API response time > 500ms, Error rate > 1%, Database connection pool exhaustion.

#### 3. Logging
- Centralized logging with ELK stack (Elasticsearch, Logstash, Kibana).
- Structured JSON logging with correlation IDs.
- Log levels: ERROR for failures, WARN for anomalies, INFO for audit trail.

#### 4. Error Handling
- Implement circuit breakers (Resilience4j) for database failures.
- Add retry logic with exponential backoff.
- Create dead-letter queue for failed message operations.

#### 5. Backup & Recovery
- Daily automated database backups with 30-day retention.
- Point-in-time recovery (PITR) capability.
- Regular disaster recovery drills.

### Scalability
Current State: Single Docker container.

#### Production Improvements:

#### 1. Horizontal Scaling
- Deploy multiple application instances behind load balancer.
- Use Kubernetes for auto-scaling based on CPU/memory.
- Implement stateless application design (JWT tokens, no sessions).
#### 2. Database Scaling
- PostgreSQL primary with read replicas.
- Partition large tables by date (e.g., monthly message partitions).
- Consider sharding by consultation_id for 100M+ messages.

#### 3. Message Queue
- Introduce RabbitMQ/Kafka for async message processing.
- Decouple message storage from real-time notifications.
- Enable webhook delivery for external systems.

#### 4. CDN & Caching
- Use CloudFront/Cloudflare for static content.
- Implement distributed caching with Redis Cluster.
- Add edge caching for read-heavy endpoints.

### Data Integrity
Current State: Basic foreign key constraints.

#### Production Improvements:
#### 1. Validation
- Add database constraints (CHECK, NOT NULL).
- Implement optimistic locking (version fields) to prevent race conditions.
- Add soft deletes instead of hard deletes (retain audit trail).

#### 2. Transactions
- Wrap multi-step operations in database transactions.
- Implement two-phase commit for distributed transactions.
- Add idempotency keys to prevent duplicate message creation.

#### 3. Audit Trail
- Log all create/update/delete operations with user ID and timestamp.
- Implement event sourcing for message history.
- Never delete data - use status flags (ACTIVE, ARCHIVED, DELETED).

#### 4. Data Validation
- Validate message content length (max 10,000 characters).
- Sanitize and validate author IDs against User table.
- Prevent future timestamps (timestamp must be <= now).

### Compliance (Healthcare-Specific)
Current State: No compliance features.

#### Production Improvements:
#### 1. HIPAA Technical Safeguards
- Implement access controls (minimum necessary rule).
- Add audit controls (track all PHI access).
- Implement integrity controls (prevent data tampering).
- Add transmission security (encrypt data in transit).

#### 2. Data Governance
- Add consent management (patient consent to share data).
- Implement right-to-delete (GDPR/CCPA compliance).
- Add data export functionality (patient data portability).

#### 3. Audit & Reporting
- Generate compliance reports (who accessed what data).
- Implement breach notification system.
- Add automated compliance checks.

#### 4. Business Continuity
- Define RTO (Recovery Time Objective): < 4 hours.
- Define RPO (Recovery Point Objective): < 15 minutes.
- Document incident response procedures. 


## Testing the Application

```bash

# Test 1: Get messages (chronological order)
curl http://localhost:8080/api/consultations/1/messages

# Test 2: Filter by doctor
curl "http://localhost:8080/api/consultations/1/messages?role=DOCTOR"

# Test 3: Add new message
curl -X POST http://localhost:8080/api/consultations/1/messages \
  -H "Content-Type: application/json" \
  -d '{"author":"P123","authorRole":"PATIENT","content":"Test message"}'

# Test 4: Error handling (invalid ID)
curl http://localhost:8080/api/consultations/999/messages
```

## Project Structure
```graphql
backend/
├── src/main/java/com/telemedicine/backend/
│   ├── BackendApplication.java          # Main Spring Boot application
│   ├── ResponseBody.java                # Common response wrapper
│   ├── controller/
│   │   ├── ConsultationController.java  # REST endpoints for consultations
│   │   └── MessageController.java       # REST endpoints for messages
│   ├── service/
│   │   ├── ConsultationService.java     # Business logic for consultations
│   │   └── MessageService.java          # Business logic for messages
│   ├── dto/
│   │   ├── ConsultationRequest.java     # Request payload for creating consultations
│   │   └── MessageRequest.java          # Request payload for creating messages
│   ├── entity/
│   │   ├── Consultation.java            # JPA entity for consultations
│   │   └── Message.java                 # JPA entity for messages
│   ├── exception/
│   │   └── GlobalExceptionHandler.java  # Centralized error handling
│   └── repository/
│       ├── ConsultationRepository.java  # Data access for consultations
│       └── MessageRepository.java       # Data access for messages
├── src/main/resources/
│   ├── application.properties           # Spring Boot configuration
│   └── data.sql                         # Seed data (2 consultations, 14 messages)
├── Dockerfile                           # Multi-stage Docker build
├── docker-compose.yml                   # Docker orchestration
└── pom.xml                              # Maven dependencies
```

## Technologies Used
- #### Java 17 - Programming language
- #### Spring Boot 3.3.0 - Application framework
- #### PostgreSQL 16 - Relational database
- #### Hibernate/JPA - Object-relational mapping
- #### Lombok - Reduces boilerplate code
- #### Jakarta Validation - Input validation
- #### Docker & Docker Compose - Containerization
- #### Maven - Build tool

## Development Notes
### Assumptions Made
- Patient and Doctor IDs are simple strings (P123, D456) rather than full User entities.
- Timestamps are server-generated to prevent client-side manipulation.
- Author role validation ensures only PATIENT or DOCTOR are allowed.
- Messages are stored as plain text (no rich text formatting).
- No authentication required per challenge instructions.

### What I'd Change with More Time
1. Add comprehensive unit tests (JUnit 5 + Mockito).
2. Implement pagination for message retrieval.
3. Add message search functionality (search by content).
4. Create integration tests with TestContainers.
5. Add API versioning (/api/v1/...)
6. Implement message editing/deletion endpoints.
7. Add WebSocket support for real-time message delivery.
8. Create Swagger/OpenAPI documentation.

## Contact
Name: Dhruva Patil

Email: dhruva.patil@dal.ca

GitHub: DhRuva-1509

## License
### This project was created as part of a coding challenge for PRAXES.

Thank you for reviewing my submission! I focused on creating a clean, working solution that demonstrates RESTful API design, proper data modeling, and awareness of production concerns. I'm excited about the opportunity to work with the Praxes team on healthcare software.
