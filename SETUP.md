# WARRIORFOOT - Setup Guide

## Prerequisites
- Java 17+
- Node.js 18+
- Docker + Docker Compose
- Gradle (included via wrapper)

## Quick Start

### 1. Start Infrastructure
```bash
docker-compose up -d
```

This starts PostgreSQL (port 5432) and Redis (port 6379).

### 2. Start Backend
```bash
cd backend
./gradlew bootRun
```

Backend runs on http://localhost:8080

**Note:** If you don't have the Gradle wrapper (`gradlew`), run:
```bash
gradle wrapper
```

### 3. Start Frontend
```bash
cd frontend
npm install  # First time only
npm run dev
```

Frontend runs on http://localhost:5173

## Testing the Application

1. Open http://localhost:5173
2. Register a new account
3. You'll be automatically logged in and redirected to the home page
4. A league with 32 teams and 704 players (22 per team) will be generated
5. You'll be assigned a random team in Division 4

## Database Access

PostgreSQL is accessible at:
- Host: localhost
- Port: 5432
- Database: warriorfoot
- User: dev
- Password: devpass

## Features Implemented (Epic 0-4)

✅ **Epic 0: Foundation**
- Docker infrastructure (PostgreSQL + Redis)
- Spring Boot backend with JPA/Hibernate
- React frontend with TypeScript + Vite
- Database schema with Flyway migrations

✅ **Epic 1: Identity & League Genesis**
- User registration with validation
- Automatic league generation (32 teams, 4 divisions)
- Team generation with random names and colors
- Player generation with Gaussian distribution by division
- Session management with Redis (24h TTL)
- Login/Logout functionality

✅ **Epic 4: Player Generation**
- 22 players per team (2 GK, 6 DF, 8 MF, 6 FW)
- Attributes based on division level (Division 1: ~80 overall, Division 4: ~55 overall)
- Market value calculation based on overall, age, and position

## Architecture

### Backend
- **Framework:** Spring Boot 3
- **Database:** PostgreSQL (JPA/Hibernate)
- **Cache:** Redis (Lettuce)
- **Security:** BCrypt password hashing
- **API:** REST controllers

### Frontend
- **Framework:** React 18 + TypeScript
- **Build:** Vite
- **State:** Zustand (with persistence)
- **Styling:** TailwindCSS
- **Routing:** React Router v6

## Project Structure

```
/backend
  /src/main/java/com/warriorfoot/api
    /config         - Redis, Security, CORS
    /controller     - REST endpoints
    /model
      /entity       - JPA entities
      /dto          - Request/Response DTOs
    /repository     - JPA repositories
    /service        - Business logic
    /util           - TeamFactory, PlayerFactory
  /src/main/resources
    /db/migration   - Flyway SQL migrations
    application.yml

/frontend
  /src
    /api           - HTTP client
    /pages         - Register, Login, Home
    /store         - Zustand state management
```

## Next Steps (Not Implemented)

- Epic 2: Invite system (email sending)
- Epic 3: Multi-league context switching
- Epic 5: Match simulation with WebSockets
- Team roster viewing
- Player management
- League standings
- Tactical lineup

## Stopping the Application

```bash
# Stop Docker containers
docker-compose down

# Backend: Ctrl+C in terminal
# Frontend: Ctrl+C in terminal
```
