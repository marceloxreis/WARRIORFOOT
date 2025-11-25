#!/bin/bash

set -e

RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
NC='\033[0m'

echo "========================================="
echo "  WARRIORFOOT - Automated Setup"
echo "========================================="
echo ""

PROJECT_DIR="/Users/marceloreis/Desktop/Projeto 2.0"
cd "$PROJECT_DIR"

echo "${YELLOW}Step 1: Checking prerequisites...${NC}"

if ! command -v brew &> /dev/null; then
    echo "${RED}Homebrew not found. Installing Homebrew...${NC}"
    /bin/bash -c "$(curl -fsSL https://raw.githubusercontent.com/Homebrew/install/HEAD/install.sh)"
else
    echo "${GREEN}✓ Homebrew installed${NC}"
fi

if ! command -v java &> /dev/null; then
    echo "${YELLOW}Installing Java 17...${NC}"
    brew install openjdk@17
    sudo ln -sfn /opt/homebrew/opt/openjdk@17/libexec/openjdk.jdk /Library/Java/JavaVirtualMachines/openjdk-17.jdk
else
    echo "${GREEN}✓ Java installed${NC}"
fi

if ! command -v node &> /dev/null; then
    echo "${YELLOW}Installing Node.js...${NC}"
    brew install node
else
    echo "${GREEN}✓ Node.js installed${NC}"
fi

if ! command -v gradle &> /dev/null; then
    echo "${YELLOW}Installing Gradle...${NC}"
    brew install gradle
else
    echo "${GREEN}✓ Gradle installed${NC}"
fi

echo ""
echo "${YELLOW}Step 2: Installing PostgreSQL...${NC}"
if ! brew list postgresql@16 &> /dev/null; then
    brew install postgresql@16
    echo "${GREEN}✓ PostgreSQL 16 installed${NC}"
else
    echo "${GREEN}✓ PostgreSQL 16 already installed${NC}"
fi

echo ""
echo "${YELLOW}Step 3: Installing Redis...${NC}"
if ! brew list redis &> /dev/null; then
    brew install redis
    echo "${GREEN}✓ Redis installed${NC}"
else
    echo "${GREEN}✓ Redis already installed${NC}"
fi

echo ""
echo "${YELLOW}Step 4: Starting PostgreSQL...${NC}"
brew services start postgresql@16
sleep 3
echo "${GREEN}✓ PostgreSQL started${NC}"

echo ""
echo "${YELLOW}Step 5: Starting Redis...${NC}"
brew services start redis
sleep 2
echo "${GREEN}✓ Redis started${NC}"

echo ""
echo "${YELLOW}Step 6: Creating database...${NC}"
PG_BIN="/opt/homebrew/opt/postgresql@16/bin"
export PATH="$PG_BIN:$PATH"
if $PG_BIN/psql postgres -lqt | cut -d \| -f 1 | grep -qw warriorfoot; then
    echo "${GREEN}✓ Database 'warriorfoot' already exists${NC}"
else
    $PG_BIN/createdb warriorfoot
    echo "${GREEN}✓ Database 'warriorfoot' created${NC}"
fi

echo ""
echo "${YELLOW}Step 7: Updating backend configuration...${NC}"
CURRENT_USER=$(whoami)
cat > "$PROJECT_DIR/backend/src/main/resources/application.yml" << EOF
spring:
  application:
    name: warriorfoot-api
  
  datasource:
    url: jdbc:postgresql://localhost:5432/warriorfoot
    username: $CURRENT_USER
    password: 
    driver-class-name: org.postgresql.Driver
  
  jpa:
    hibernate:
      ddl-auto: validate
    show-sql: false
    properties:
      hibernate:
        format_sql: true
        dialect: org.hibernate.dialect.PostgreSQLDialect
  
  data:
    redis:
      host: localhost
      port: 6379
      timeout: 2000ms
      lettuce:
        pool:
          max-active: 8
          max-idle: 8
          min-idle: 0
  
  flyway:
    enabled: true
    baseline-on-migrate: true
    locations: classpath:db/migration
  
  mail:
    host: \${MAIL_HOST:smtp.gmail.com}
    port: \${MAIL_PORT:587}
    username: \${MAIL_USERNAME:}
    password: \${MAIL_PASSWORD:}
    properties:
      mail:
        smtp:
          auth: true
          starttls:
            enable: true

server:
  port: 8080
  error:
    include-message: always
    include-binding-errors: always

logging:
  level:
    com.warriorfoot: DEBUG
    org.springframework.web: INFO
    org.hibernate: INFO
EOF
echo "${GREEN}✓ Backend configuration updated for local database${NC}"

echo ""
echo "${YELLOW}Step 8: Setting up Gradle wrapper...${NC}"
cd "$PROJECT_DIR/backend"
if [ ! -f "gradlew" ]; then
    gradle wrapper
    chmod +x gradlew
    echo "${GREEN}✓ Gradle wrapper created${NC}"
else
    echo "${GREEN}✓ Gradle wrapper already exists${NC}"
fi

echo ""
echo "${YELLOW}Step 9: Installing frontend dependencies...${NC}"
cd "$PROJECT_DIR/frontend"
npm install
echo "${GREEN}✓ Frontend dependencies installed${NC}"

echo ""
echo "${GREEN}========================================="
echo "  Setup Complete! Starting Application..."
echo "=========================================${NC}"
echo ""

echo "${YELLOW}Starting backend in background...${NC}"
cd "$PROJECT_DIR/backend"
./gradlew bootRun > "$PROJECT_DIR/backend.log" 2>&1 &
BACKEND_PID=$!
echo "${GREEN}✓ Backend started (PID: $BACKEND_PID)${NC}"
echo "   Logs: $PROJECT_DIR/backend.log"
echo "   URL: http://localhost:8080"

echo ""
echo "${YELLOW}Waiting for backend to be ready...${NC}"
sleep 10

for i in {1..30}; do
    if curl -s http://localhost:8080/auth/login > /dev/null 2>&1; then
        echo "${GREEN}✓ Backend is ready!${NC}"
        break
    fi
    echo "   Waiting... ($i/30)"
    sleep 2
done

echo ""
echo "${YELLOW}Starting frontend...${NC}"
cd "$PROJECT_DIR/frontend"
npm run dev &
FRONTEND_PID=$!
echo "${GREEN}✓ Frontend started (PID: $FRONTEND_PID)${NC}"
echo "   URL: http://localhost:5173"

echo ""
echo "${GREEN}========================================="
echo "  🚀 WARRIORFOOT IS RUNNING!"
echo "=========================================${NC}"
echo ""
echo "Open your browser: ${GREEN}http://localhost:5173${NC}"
echo ""
echo "Backend PID: $BACKEND_PID"
echo "Frontend PID: $FRONTEND_PID"
echo ""
echo "To stop the application:"
echo "  kill $BACKEND_PID $FRONTEND_PID"
echo ""
echo "To view backend logs:"
echo "  tail -f $PROJECT_DIR/backend.log"
echo ""
echo "Press Ctrl+C to stop this script (services will keep running)"
echo ""

wait
