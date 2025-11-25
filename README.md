# WARRIORFOOT

Multiplayer soccer management game for web.

## Stack
- **Backend:** Java 21, Spring Boot 3, WebSockets/STOMP, JPA/Hibernate
- **Frontend:** TypeScript, React 18, Vite, Zustand, TailwindCSS
- **Database:** PostgreSQL 16
- **Cache/Session:** Redis 7

## Architecture
- Backend is source of truth for all game state
- REST for static data, WebSockets for real-time match simulation
- Redis session management for horizontal scaling
- Multi-tenancy via league context switching

## Project Structure
```
/backend         - Java Spring Boot API
/frontend        - React TypeScript SPA
/infra           - Docker Compose, migrations
```

## Setup
```bash
docker-compose up -d
cd backend && ./gradlew bootRun
cd frontend && npm run dev
```
