gen-targets:
	mvn clean install -U

build-gateway:
	cd gateway; docker build -t gateway:latest .

build-cart:
	cd cart; docker build -t cart:latest .

build-checkout:
	cd checkout; docker build -t checkout:latest .

build: gen-targets build-gateway build-cart build-checkout
	@echo "Building Docker images..."

run: build
	@echo "Starting services with docker-compose..."
	docker-compose up
