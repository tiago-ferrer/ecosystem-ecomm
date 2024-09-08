#!/bin/bash

# Function to build Docker images
build_images() {

  echo "Gen targets"
  mvn clean install -U

  echo "Building Docker images..."

  # Build gateway image
  cd gateway || exit
  docker build -t gateway:latest .
  cd ..

  # Build cart image
  cd cart || exit
  docker build -t cart:latest .
  cd ..

  # Build checkout image
  cd checkout || exit
  docker build -t checkout:latest .
  cd ..

  echo "Docker images built successfully."
}

# Function to start services using docker-compose
start_services() {
  echo "Starting services with docker-compose..."
  docker-compose up
}

# Main script execution
build_images
start_services