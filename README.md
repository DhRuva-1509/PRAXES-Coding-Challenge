# Telemedicine Consultation Messages - Backend

A Spring Boot REST API for storing and retrieving messages from telemedicine consultations.

## Prerequisites

- Docker Desktop installed and running
- No other services running on ports 8080 or 5433

## Quick Start with Docker

### 1. Clone the Repository

git clone 
cd backend

### 2. Build and Run with Docker Compose
docker-compose up –build

This single command will:
- Build the Spring Boot application
- Start a PostgreSQL 16 database
- Create the necessary tables
- Load sample seed data (2 consultations with 14 messages)
- Start the application on http://localhost:8080
