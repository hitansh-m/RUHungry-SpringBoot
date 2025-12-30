# RUHungry Spring Boot Application

## Overview

This is a **Spring Boot REST API** version of the RUHungry restaurant management system. The original console application has been wrapped with modern web APIs while preserving all the custom data structures (hashtables, linked lists) and business logic.

## Architecture

```
┌─────────────────────────────────────────────────┐
│         Spring Boot REST API Layer              │
│  (Controllers, DTOs, JSON responses)            │
└─────────────────┬───────────────────────────────┘
                  │
┌─────────────────▼───────────────────────────────┐
│         Service Layer (Business Logic)          │
│  (RestaurantService - wraps RUHungry)           │
└─────────────────┬───────────────────────────────┘
                  │
┌─────────────────▼───────────────────────────────┐
│    Original RUHungry Core Logic                 │
│  (Custom hashtables, linked lists - UNCHANGED)  │
└─────────────────────────────────────────────────┘
```

## Key Features

- ✅ **REST API** - Modern HTTP endpoints for all operations
- ✅ **Original Logic Preserved** - Custom hashtables and linked lists intact
- ✅ **Swagger Documentation** - Interactive API docs at `/swagger-ui.html`
- ✅ **Web Dashboard** - Beautiful UI for testing the API
- ✅ **Spring Boot Best Practices** - Proper layering (Controller → Service → Core)

## Prerequisites

- Java 17 or higher
- Maven 3.6+

## Project Structure

```
ruhungry-springboot/
├── pom.xml                              # Maven dependencies
├── src/
│   ├── main/
│   │   ├── java/com/ruhungry/
│   │   │   ├── RUHungryApplication.java # Main Spring Boot app
│   │   │   ├── core/                    # Original RUHungry code
│   │   │   │   ├── RUHungry.java       # Modified to use InputStream
│   │   │   │   ├── Dish.java
│   │   │   │   ├── Ingredient.java
│   │   │   │   ├── MenuNode.java
│   │   │   │   ├── StockNode.java
│   │   │   │   ├── TransactionData.java
│   │   │   │   ├── TransactionNode.java
│   │   │   │   ├── Party.java
│   │   │   │   └── Queue.java
│   │   │   ├── controller/              # REST endpoints
│   │   │   │   ├── MenuController.java
│   │   │   │   ├── OrderController.java
│   │   │   │   └── InventoryController.java
│   │   │   ├── service/                 # Business logic wrapper
│   │   │   │   └── RestaurantService.java
│   │   │   └── dto/                     # API models
│   │   │       ├── DishDTO.java
│   │   │       ├── OrderRequest.java
│   │   │       ├── OrderResponse.java
│   │   │       ├── StockResponse.java
│   │   │       └── RestockRequest.java
│   │   └── resources/
│   │       ├── application.properties   # Configuration
│   │       ├── data/                    # Input files
│   │       │   ├── menu.in
│   │       │   ├── stock.in
│   │       │   └── tables1.in
│   │       └── static/
│   │           └── index.html           # Web dashboard
│   └── test/
│       └── java/com/ruhungry/
└── README.md
```

## What Changed from Original?

### Minimal Changes to Core Code ✅

1. **Package Declaration**: Added `package com.ruhungry.core;`
2. **File Loading**: Changed from `StdIn` to `InputStream`
   - `menu(String inputFile)` → `menu(InputStream inputStream)`
   - `createStockHashTable(String inputFile)` → `createStockHashTable(InputStream inputStream)`
   - `createTables(String inputFile)` → `createTables(InputStream inputStream)`

### Everything Else Stays the Same! ✅

- ✅ All hashtable logic
- ✅ All linked list implementations  
- ✅ All algorithms and business rules
- ✅ Order processing
- ✅ Inventory management
- ✅ Transaction tracking

## How to Run

### 1. Build the Project

```bash
cd ruhungry-springboot
mvn clean install
```

### 2. Run the Application

```bash
mvn spring-boot:run
```

The application will start on **http://localhost:8080**

### 3. Access the Application

- **Web Dashboard**: http://localhost:8080
- **Swagger API Docs**: http://localhost:8080/swagger-ui.html
- **API Documentation**: http://localhost:8080/api-docs

## Running with Docker 🐳

No Java installation required - just Docker!

### Option 1: Docker Compose (Recommended)

```bash
# Build and run
docker-compose up --build

# Run in background
docker-compose up -d --build

# Stop
docker-compose down
```

### Option 2: Docker CLI

```bash
# Build the image
docker build -t ruhungry-app .

# Run the container
docker run -p 8080:8080 ruhungry-app

# Run in background
docker run -d -p 8080:8080 --name ruhungry ruhungry-app

# Stop and remove
docker stop ruhungry && docker rm ruhungry
```

Access the app at **http://localhost:8080**

## API Endpoints

### Menu APIs

```http
GET    /api/menu                      # Get all dishes
GET    /api/menu/categories           # Get all categories
GET    /api/menu/category/{name}      # Get dishes by category
```

### Order APIs

```http
POST   /api/orders                    # Place an order
GET    /api/orders/profit             # Get current profit
```

**Example Order Request:**
```json
{
  "dishName": "Hamburger",
  "quantity": 2
}
```

### Inventory APIs

```http
GET    /api/inventory                 # Get all ingredients
GET    /api/inventory/{name}          # Get stock info
PUT    /api/inventory/{name}?amount=X # Update stock
POST   /api/inventory/restock         # Restock ingredient
POST   /api/inventory/donate          # Donate ingredient
```

## Testing with cURL

### Get All Dishes
```bash
curl http://localhost:8080/api/menu
```

### Place an Order
```bash
curl -X POST http://localhost:8080/api/orders \
  -H "Content-Type: application/json" \
  -d '{"dishName": "Hamburger", "quantity": 2}'
```

### Get Current Profit
```bash
curl http://localhost:8080/api/orders/profit
```

### Get Stock Level
```bash
curl http://localhost:8080/api/inventory/Lettuce
```

### Restock Ingredient
```bash
curl -X POST http://localhost:8080/api/inventory/restock \
  -H "Content-Type: application/json" \
  -d '{"ingredientName": "Lettuce", "quantity": 50}'
```

## Technologies Used

- **Spring Boot 3.1.5** - Main framework
- **Spring Web** - REST API support
- **Spring Validation** - Input validation
- **Spring DevTools** - Hot reload during development
- **Springdoc OpenAPI** - Swagger documentation
- **Maven** - Build tool
- **Docker** - Containerization

## Interview Talking Points 💼

> "I built RUHungry initially as a console application with custom data structures like hashtables and linked lists. To demonstrate my ability to modernize legacy systems, I migrated it to Spring Boot by wrapping the core logic with REST APIs. The original hashtable and linked list implementations remained unchanged, showcasing how solid OOP design allows for easy framework integration. This gave me hands-on experience with:
> - RESTful API design
> - Spring Boot architecture (Controllers, Services, DTOs)
> - Legacy system modernization
> - Maintaining backward compatibility while adding new features"

## Resume Bullet Points 📝

- Migrated Java console application to **Spring Boot REST API** with minimal code changes
- Designed RESTful endpoints following **MVC architecture** (Controllers, Services, DTOs)
- Preserved original custom **hashtable and linked list** data structures while adding modern web layer
- Implemented **Swagger/OpenAPI** documentation for API discoverability
- Created responsive web dashboard for real-time restaurant operations monitoring
- **Containerized application with Docker** for consistent deployment across environments

## Future Enhancements

- [ ] Add database persistence (H2/PostgreSQL)
- [ ] Implement Spring Security for authentication
- [ ] Add unit and integration tests
- [x] Create Docker containerization 
- [ ] Add transaction history endpoint
- [ ] Implement WebSocket for real-time updates

## License

Educational project - Original assignment from Rutgers University

## Author

Migrated to Spring Boot - [Your Name]  
Original RUHungry - Mary Buist & Kushi Sharma
