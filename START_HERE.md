# 🚀 WARRIORFOOT - Quick Start

## What's Ready

✅ Full-stack application with:
- **Backend:** Spring Boot 3 + PostgreSQL + Redis
- **Frontend:** React 18 + TypeScript + TailwindCSS
- **Features:** User registration, login, automatic league generation with 32 teams and 704 players

## Start the Application

### Terminal 1: Infrastructure
```bash
docker compose up -d
```

### Terminal 2: Backend
```bash
cd backend
./gradlew bootRun
```

If `./gradlew` doesn't exist, install Gradle wrapper:
```bash
cd backend
gradle wrapper
./gradlew bootRun
```

### Terminal 3: Frontend
```bash
cd frontend
npm run dev
```

## Access the Application

Open your browser: **http://localhost:5173**

1. Click "Register" to create a new account
2. Fill in your details (minimum 6 character password)
3. You'll be automatically logged in
4. Your personal league with 32 teams is generated instantly

## What Happens When You Register?

The system automatically:
- Creates your user account with hashed password (BCrypt)
- Generates a league with 32 teams (8 teams per division 1-4)
- Creates 704 players (22 per team) with Gaussian-distributed attributes
- Assigns you a random team in Division 4
- Creates a Redis session (24h validity)
- Redirects you to the home page

## Player Generation Logic

Players are generated with attributes based on division:
- **Division 1:** Average overall 80 (std dev 5)
- **Division 2:** Average overall 70 (std dev 6)
- **Division 3:** Average overall 60 (std dev 7)
- **Division 4:** Average overall 55 (std dev 8)

Market value = (Overall² × 1000) × Position Multiplier × Age Factor

## Tech Stack

**Backend:**
- Java 17, Spring Boot 3.2
- PostgreSQL 16 (database)
- Redis 7 (sessions)
- JPA/Hibernate, Flyway migrations
- BCrypt password hashing

**Frontend:**
- React 18 + TypeScript
- Vite (build tool)
- Zustand (state management with localStorage persistence)
- React Router v6
- TailwindCSS

## Git History

All changes are committed:
```bash
git log --oneline
```

## Troubleshooting

**Backend won't start:**
- Ensure PostgreSQL is running on port 5432
- Check `backend/src/main/resources/application.yml` for DB config

**Frontend API errors:**
- Backend must be running on http://localhost:8080
- Check CORS configuration in `backend/src/main/java/com/warriorfoot/api/config/CorsConfig.java`

**Docker not starting:**
- Ensure Docker Desktop is running
- Try: `docker compose down` then `docker compose up -d`

## Database Access

Connect to PostgreSQL to inspect data:
```bash
psql -h localhost -U dev -d warriorfoot
# Password: devpass
```

View your generated teams:
```sql
SELECT name, division_level FROM teams ORDER BY division_level, name;
```

View player distribution:
```sql
SELECT position, COUNT(*), AVG(overall)::int as avg_overall 
FROM players 
GROUP BY position;
```

## Next Features to Implement

- Team roster viewing
- Player lineup management
- Match simulation (WebSocket)
- League standings
- Friend invites (Epic 2)
- Multi-league context switching (Epic 3)

## Full Documentation

See `SETUP.md` for detailed architecture and implementation notes.
