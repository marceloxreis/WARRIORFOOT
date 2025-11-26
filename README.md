# WARRIORFOOT - Football Manager Game

A modern web-based football management simulation game where you manage teams, develop players, and compete across multiple divisions.

## Features

### Current Features

  - **Multi-League System**: Create and manage multiple league saves
  - **Team Management**: Control teams across 4 divisions (32 teams total)
  - **Player Development**: 22 players per team with detailed stats (40+ attributes)
  - **Position-Based Stats**: Specialized statistics for GK, DF, MF, and FW positions
  - **League System**: 4-tier division structure with 8 teams per division
  - **Multiplayer Support**: Invite friends via email to join your leagues
  - **User Authentication**: Secure registration and login with session management
  - **Responsive UI**: Modern, mobile-friendly interface with TailwindCSS

### Player Features

  - **Detailed Stats**: Pace, Shooting, Passing, Dribbling, Defending, Physical
  - **Goalkeeper Specialization**: Diving, Handling, Kicking, Reflexes, Speed, Positioning
  - **Realistic Generation**: Position-based stat distribution with variance
  - **Market Value**: Dynamic player valuation based on overall rating, age, and position
  - **Age System**: Players aged 16-40 with age-based market modifiers

### League Features

  - **Custom League Names**: Name your leagues for easy identification
  - **Division Structure**: 4 divisions with automatic team distribution
  - **Manager Visibility**: See which teams are managed by other players
  - **League Switching**: Easily switch between multiple league saves
  - **Invite System**: Email-based invitations with 7-day expiration

## Tech Stack

### Backend

  - **Java 17** with **Spring Boot 3.2.0**
  - **PostgreSQL 16** - Primary database
  - **Redis 7** - Session management & caching
  - **Flyway** - Database migrations
  - **BCrypt** - Password hashing
  - **JavaMailSender** - Email invitations
  - **Spring Security** - Authentication & authorization
  - **Hibernate/JPA** - ORM
  - **Gradle** - Build tool

### Frontend

  - **React 19** with **TypeScript 5.9**
  - **Vite** - Build tool & dev server
  - **React Router 7** - Client-side routing
  - **Zustand** - State management with persistence
  - **TailwindCSS 4** - Styling
  - **Zod** - Runtime type validation
  - **ESLint** - Code linting

### Infrastructure

  - **Docker Compose** - PostgreSQL + Redis containers
  - **Hot Reload** - Both frontend and backend support live reload

## Prerequisites

  - **Java 17** or higher
  - **Node.js 18** or higher
  - **Docker** and **Docker Compose**
  - **Gradle** (included via wrapper)

## Quick Start

### 1\. Clone the Repository

```bash
git clone https://github.com/marceloxreis/WARRIORFOOT.git
cd WARRIORFOOT
```

### 2\. Environment Setup

**Backend (.env in root):**

```bash
cp .env.example .env
# Edit .env with your email credentials for invitations
```

**Frontend:**

```bash
cd frontend
cp .env.example .env
# Default values should work for local development
```

### 3\. Start Services

**Option A: Automated Setup (Recommended)**

```bash
# From project root
chmod +x setup-and-start.sh
./setup-and-start.sh
```

cd "/Users/marceloreis/Desktop/Projeto 2.0" && ./setup-and-start.sh

This script will:

  - Start Docker containers (PostgreSQL + Redis)
  - Run database migrations
  - Start the backend server
  - Install frontend dependencies
  - Start the frontend dev server

**Option B: Manual Setup**

```bash
# 1. Start Docker containers
docker-compose up -d

# 2. Start backend (in one terminal)
cd backend
./gradlew bootRun

# 3. Start frontend (in another terminal)
cd frontend
npm install
npm run dev
```

### 4\. Access the Application

  - **Frontend**: http://localhost:5173
  - **Backend API**: http://localhost:8080
  - **PostgreSQL**: localhost:5432
  - **Redis**: localhost:6379

### 5\. Create Your First Account

1.  Navigate to http://localhost:5173
2.  Click "Register"
3.  Fill in your details
4.  A default league will be created automatically
5.  You'll be assigned a random Division 4 team

## Project Structure

```
WARRIORFOOT/
├── backend/                    # Java Spring Boot backend
│   ├── src/main/java/com/warriorfoot/api/
│   │   ├── config/            # Configuration classes
│   │   │   ├── GameConstants.java    # Game configuration constants
│   │   │   ├── SecurityConfig.java   # Security configuration
│   │   │   ├── CorsConfig.java       # CORS settings
│   │   │   └── RedisConfig.java      # Redis session config
│   │   ├── controller/        # REST API endpoints
│   │   │   ├── AuthController.java   # Login/Register
│   │   │   ├── LeagueController.java # League management
│   │   │   ├── TeamController.java   # Team operations
│   │   │   ├── PlayerController.java # Player details
│   │   │   └── InviteController.java # Friend invitations
│   │   ├── service/           # Business logic
│   │   │   ├── AuthService.java      # Authentication
│   │   │   ├── LeagueService.java    # League operations
│   │   │   ├── TeamService.java      # Team management
│   │   │   ├── PlayerService.java    # Player operations
│   │   │   ├── InviteService.java    # Invitation system
│   │   │   ├── SessionService.java   # Session management
│   │   │   └── EmailService.java     # Email sender
│   │   ├── model/
│   │   │   ├── entity/        # JPA entities
│   │   │   └── dto/           # Data transfer objects
│   │   ├── repository/        # JPA repositories
│   │   └── util/              # Utility classes
│   │       ├── PlayerFactory.java    # Player generation
│   │       ├── TeamFactory.java      # Team generation
│   │       └── StatWeights.java      # Overall calculations
│   └── src/main/resources/
│       ├── db/migration/      # Flyway migrations
│       └── application.yml    # Application config
│
├── frontend/                   # React TypeScript frontend
│   ├── src/
│   │   ├── api/               # API clients
│   │   │   ├── client.ts      # Auth API
│   │   │   ├── leagueApi.ts   # League data
│   │   │   ├── leagueManagementApi.ts # League CRUD
│   │   │   ├── teamApi.ts     # Team data
│   │   │   └── playerApi.ts   # Player data
│   │   ├── components/        # Reusable components
│   │   │   ├── ConfirmDialog.tsx
│   │   │   └── InviteDialog.tsx
│   │   ├── config/            # Configuration
│   │   │   └── api.ts         # API config & helpers
│   │   ├── pages/             # Page components
│   │   │   ├── Login.tsx      # Login page
│   │   │   ├── Register.tsx   # Registration
│   │   │   ├── Leagues.tsx    # League switcher
│   │   │   ├── Home.tsx       # User dashboard
│   │   │   ├── Dashboard.tsx  # League dashboard
│   │   │   ├── Team.tsx       # Team roster
│   │   │   ├── PlayerDetails.tsx # Player stats
│   │   │   └── AcceptInvite.tsx  # Accept invitation
│   │   ├── store/             # State management
│   │   │   └── authStore.ts   # Zustand auth store
│   │   ├── types/             # TypeScript types
│   │   ├── App.tsx            # Router setup
│   │   └── main.tsx           # Entry point
│   ├── index.html
│   ├── tailwind.config.js
│   ├── vite.config.ts
│   └── package.json
│
├── docker-compose.yml          # Docker services
├── .env.example                # Environment variables template
├── setup-and-start.sh          # Automated setup script
├── start-dev.sh                # Quick start script
└── README.md                   # This file
```

## Configuration

### Game Constants

Key game parameters are defined in `backend/src/main/java/com/warriorfoot/api/config/GameConstants.java`:

```java
// League Structure
TOTAL_TEAMS_PER_LEAGUE = 32
TOTAL_DIVISIONS = 4
TEAMS_PER_DIVISION = 8
STARTING_DIVISION = 4          // New users start here

// Team Composition
GOALKEEPERS_PER_TEAM = 2
DEFENDERS_PER_TEAM = 6
MIDFIELDERS_PER_TEAM = 8
FORWARDS_PER_TEAM = 6

// Player Attributes
MIN_PLAYER_AGE = 16
MAX_PLAYER_AGE = 40
MIN_PLAYER_STAT = 35           // Minimum for any attribute
MAX_PLAYER_STAT = 99
MIN_PLAYER_OVERALL = 40
MAX_PLAYER_OVERALL = 99

// Features
INVITE_EXPIRATION_DAYS = 7
```

### Environment Variables

**Backend (.env)**

```env
# Database
DB_URL=jdbc:postgresql://localhost:5432/warriorfoot
DB_USERNAME=postgres
DB_PASSWORD=postgres

# Redis
REDIS_HOST=localhost
REDIS_PORT=6379

# Email (for invitations)
MAIL_HOST=smtp.gmail.com
MAIL_PORT=587
MAIL_USERNAME=your-email@gmail.com
MAIL_PASSWORD=your-app-password
MAIL_FROM=noreply@warriorfoot.com
```

**Frontend (.env)**

```env
VITE_API_URL=http://localhost:8080
```

## API Endpoints

### Authentication

  - `POST /auth/register` - Create new account
  - `POST /auth/login` - Login
  - `POST /auth/logout` - Logout

### Leagues

  - `GET /leagues/user/list` - Get user's leagues
  - `POST /leagues/create` - Create new league
  - `GET /leagues/{id}` - Get league dashboard
  - `DELETE /leagues/{id}` - Delete league (creator only)
  - `POST /leagues/{id}/leave` - Leave league

### Teams

  - `GET /teams/{id}` - Get team details
  - `GET /teams/{id}/players` - Get team roster

### Players

  - `GET /players/{id}` - Get player details

### Invitations

  - `POST /invites/send` - Send league invitation
  - `GET /invites/{token}` - Get invitation details
  - `POST /invites/{token}/accept` - Accept invitation

## Database Schema

### Core Tables

  - **users** - User accounts
  - **leagues** - League instances
  - **teams** - 32 teams per league
  - **players** - 22 players per team (704 per league)
  - **user\_leagues** - Links users to leagues/teams
  - **invites** - Email invitations

### Relationships

  - Each league has 32 teams across 4 divisions
  - Each team has 22 players (2 GK, 6 DF, 8 MF, 6 FW)
  - Users can join multiple leagues
  - One user per team per league

## Development

### Running Tests

```bash
# Backend
cd backend
./gradlew test

# Frontend
cd frontend
npm test
```

### Code Style

```bash
# Frontend linting
cd frontend
npm run lint
```

### Database Migrations

Migrations are handled by Flyway and run automatically on startup.

To create a new migration:

```bash
# Create file: backend/src/main/resources/db/migration/V{next_number}__{description}.sql
```

### Hot Reload

Both backend and frontend support hot reload:

  - **Backend**: Spring DevTools auto-restarts on code changes
  - **Frontend**: Vite HMR instantly reflects changes

## Security Notes

  - Passwords are hashed using BCrypt
  - Sessions stored in Redis with 24-hour TTL
  - CORS configured for local development
  - Email invitations use cryptographically secure tokens

**Production Checklist:**

  - [ ] Enable CSRF protection
  - [ ] Use HTTPS
  - [ ] Configure rate limiting
  - [ ] Set up proper CORS origins
  - [ ] Use environment variables for all secrets
  - [ ] Enable database SSL
  - [ ] Configure Redis password
  - [ ] Set up email authentication properly

## Planned Features

### Coming Soon

  - **Match Simulation**: Simulate matches between teams
  - **League Standings**: View league tables and positions
  - **Tactics & Formations**: Set your team's strategy
  - **Transfer Market**: Buy and sell players
  - **Player Development**: Train and improve your squad
  - **Match History**: Track results and statistics
  - **Achievements**: Unlock rewards for accomplishments
  - **Live Matches**: Real-time match simulation
  - **Financial Management**: Team budget and wages
  - **Youth Academy**: Develop young players

## License

This project is for educational and personal use.

## Known Issues

  - Email service requires Gmail App Password for production use
  - No pagination on large datasets
  - Match simulation not yet implemented
  - No player transfer system yet

## Tips

  - **First League**: Automatically created with "My First League" name
  - **Starting Division**: All new users start in Division 4
  - **Player Ages**: Younger players (under 24) have higher market values
  - **Invite Expiry**: Invitations expire after 7 days
  - **Multiple Leagues**: Switch between leagues via the "My Leagues" page
  - **Team Assignment**: Random Division 4 team assigned on registration/invite

-----

Would you like me to optimize the `setup-and-start.sh` script or review the Java/React integration based on this structure?
