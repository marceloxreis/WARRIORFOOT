#!/bin/bash

echo "Starting WARRIORFOOT Development Environment..."
echo ""

echo "1. Starting Docker containers (PostgreSQL + Redis)..."
docker-compose up -d

echo ""
echo "2. Waiting for databases to be ready..."
sleep 5

echo ""
echo "3. Loading email configuration..."
PROJECT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
if [ -f "$PROJECT_DIR/.env" ]; then
    export $(cat "$PROJECT_DIR/.env" | grep -v '^#' | xargs)
    echo "✓ Email configuration loaded from .env"
else
    echo "⚠ Warning: .env file not found. Email features will not work."
    echo "   Copy .env.example to .env and configure your email credentials."
fi

echo ""
echo "4. Backend will be available at: http://localhost:8080"
echo "5. Frontend will be available at: http://localhost:5173"
echo ""
echo "To start the backend: cd backend && ./gradlew bootRun"
echo "To start the frontend: cd frontend && npm run dev"
echo ""
echo "Docker containers are running. Use 'docker-compose down' to stop them."
