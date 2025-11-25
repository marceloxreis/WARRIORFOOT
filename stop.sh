#!/bin/bash

GREEN='\033[0;32m'
YELLOW='\033[1;33m'
NC='\033[0m'

echo "${YELLOW}Stopping WARRIORFOOT...${NC}"

echo ""
echo "Stopping backend processes..."
pkill -f "gradle.*bootRun" || true
pkill -f "java.*warriorfoot" || true

echo "Stopping frontend processes..."
pkill -f "vite" || true

echo ""
echo "${GREEN}✓ Application stopped${NC}"
echo ""
echo "Database services (PostgreSQL and Redis) are still running."
echo "To stop them:"
echo "  brew services stop postgresql@16"
echo "  brew services stop redis"
