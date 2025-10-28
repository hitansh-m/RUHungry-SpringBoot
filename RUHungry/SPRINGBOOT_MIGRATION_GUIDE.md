# How to Convert RUHungry to Spring Boot (Without Breaking It)

## Strategy: Wrapper Pattern

The best approach is to **wrap your existing logic** with Spring Boot REST APIs, rather than rewriting everything. This preserves your custom data structures while adding modern web capabilities.

---

## Architecture Overview

```
┌─────────────────────────────────────────────────┐
│         Spring Boot REST API Layer              │
│  (Controllers, DTOs, JSON responses)            │
└─────────────────┬───────────────────────────────┘
                  │
┌─────────────────▼───────────────────────────────┐
│         Service Layer (Business Logic)          │
│  (Wraps RUHungry methods)                       │
└─────────────────┬───────────────────────────────┘
                  │
┌─────────────────▼───────────────────────────────┐
│    Existing RUHungry Core Logic                 │
│  (Custom hashtables, linked lists - UNCHANGED)  │
└─────────────────────────────────────────────────┘
```

---

## Step-by-Step Migration

### Step 1: Create New Spring Boot Project Structure

```
ruhungry-springboot/
├── pom.xml (Spring Boot version)
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── com/ruhungry/
│   │   │       ├── RUHungryApplication.java       # Spring Boot main
│   │   │       ├── core/                          # Your existing code
│   │   │       │   ├── RUHungry.java             # COPY existing file
│   │   │       │   ├── Dish.java                  # COPY existing file
│   │   │       │   ├── Ingredient.java            # COPY existing file
│   │   │       │   ├── MenuNode.java              # COPY existing file
│   │   │       │   ├── StockNode.java             # COPY existing file
│   │   │       │   └── ... (all other core files)
│   │   │       ├── controller/                    # NEW - REST endpoints
│   │   │       │   ├── MenuController.java
│   │   │       │   ├── OrderController.java
│   │   │       │   ├── InventoryController.java
│   │   │       │   └── SeatingController.java
│   │   │       ├── service/                       # NEW - Business logic
│   │   │       │   └── RestaurantService.java
│   │   │       ├── dto/                           # NEW - API models
│   │   │       │   ├── DishDTO.java
│   │   │       │   ├── OrderRequest.java
│   │   │       │   └── StockResponse.java
│   │   │       └── config/                        # NEW - Configuration
│   │   │           └── RestaurantConfig.java
│   │   └── resources/
│   │       ├── application.properties
│   │       ├── static/                            # Optional: Frontend
│   │       └── data/                              # Your .in files
│   │           ├── menu.in
│   │           ├── stock.in
│   │           └── tables1.in
│   └── test/
│       └── java/com/ruhungry/
│           └── RUHungryApplicationTests.java
```

---

## Step 2: Update pom.xml for Spring Boot

```xml
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 
         http://maven.apache.org/xsd/maven-4.0.0.xsd">
    <modelVersion>4.0.0</modelVersion>
    
    <parent>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-parent</artifactId>
        <version>3.1.5</version>
        <relativePath/>
    </parent>

    <groupId>com.ruhungry</groupId>
    <artifactId>ruhungry-springboot</artifactId>
    <version>1.0.0</version>
    <name>RUHungry Spring Boot</name>
    <description>Food pantry simulation with Spring Boot REST API</description>

    <properties>
        <java.version>17</java.version>
    </properties>

    <dependencies>
        <!-- Spring Boot Web (REST APIs) -->
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-web</artifactId>
        </dependency>

        <!-- Spring Boot Validation -->
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-validation</artifactId>
        </dependency>

        <!-- Spring Boot DevTools (hot reload) -->
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-devtools</artifactId>
            <scope>runtime</scope>
            <optional>true</optional>
        </dependency>

        <!-- Spring Boot Test -->
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-test</artifactId>
            <scope>test</scope>
        </dependency>

        <!-- Optional: Swagger for API documentation -->
        <dependency>
            <groupId>org.springdoc</groupId>
            <artifactId>springdoc-openapi-starter-webmvc-ui</artifactId>
            <version>2.2.0</version>
        </dependency>
    </dependencies>

    <build>
        <plugins>
            <plugin>
                <groupId>org.springframework.boot</groupId>
                <artifactId>spring-boot-maven-plugin</artifactId>
            </plugin>
        </plugins>
    </build>
</project>
```

---

## Step 3: Create Spring Boot Main Application

**File: `src/main/java/com/ruhungry/RUHungryApplication.java`**

```java
package com.ruhungry;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class RUHungryApplication {
    public static void main(String[] args) {
        SpringApplication.run(RUHungryApplication.class, args);
    }
}
```

---

## Step 4: Modify Core RUHungry Class (Minimal Changes)

**Key Changes to `RUHungry.java`:**

1. **Remove StdIn/StdOut dependencies** for file loading
2. **Add resource path support** for Spring Boot classpath
3. **Keep all logic intact**

**File: `src/main/java/com/ruhungry/core/RUHungry.java`**

```java
package com.ruhungry.core;

import java.io.InputStream;
import java.util.Scanner;

// Keep all your existing logic, just replace StdIn with Scanner

public class RUHungry {
    // All your existing fields remain the same
    private String[] categoryVar;
    private MenuNode[] menuVar;
    private StockNode[] stockVar;
    private int stockVarSize;
    private TransactionNode transactionVar;
    // ... etc

    // MODIFY THIS METHOD to use InputStream instead of StdIn
    public void createStockHashTable(InputStream inputStream) {
        Scanner scanner = new Scanner(inputStream);
        stockVarSize = scanner.nextInt();
        stockVar = new StockNode[stockVarSize];
        
        while (scanner.hasNextLine()) {
            int stockId = scanner.nextInt();
            scanner.nextLine(); // consume newline
            String name = scanner.nextLine().trim();
            double cost = scanner.nextDouble();
            int amt = scanner.nextInt();
            
            Ingredient ingredient = new Ingredient(stockId, name, amt, cost);
            StockNode node = new StockNode(ingredient, null);
            addStockNode(node);
        }
        scanner.close();
    }

    // MODIFY menu() similarly
    public void menu(InputStream inputStream) {
        Scanner scanner = new Scanner(inputStream);
        // ... rest of your logic
    }

    // ALL OTHER METHODS REMAIN EXACTLY THE SAME
    // - findDish()
    // - updatePriceAndProfit()
    // - checkDishAvailability()
    // - order()
    // - donation()
    // - restock()
    // etc.
}
```

---

## Step 5: Create Service Layer (Wrapper)

**File: `src/main/java/com/ruhungry/service/RestaurantService.java`**

```java
package com.ruhungry.service;

import com.ruhungry.core.*;
import com.ruhungry.dto.*;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import javax.annotation.PostConstruct;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
public class RestaurantService {
    
    private RUHungry restaurant;

    @PostConstruct
    public void initialize() throws IOException {
        restaurant = new RUHungry();
        
        // Load data from resources folder
        ClassPathResource stockResource = new ClassPathResource("data/stock.in");
        ClassPathResource menuResource = new ClassPathResource("data/menu.in");
        ClassPathResource tablesResource = new ClassPathResource("data/tables1.in");
        
        restaurant.createStockHashTable(stockResource.getInputStream());
        restaurant.menu(menuResource.getInputStream());
        restaurant.createTables(tablesResource.getInputStream());
        restaurant.updatePriceAndProfit();
    }

    public List<DishDTO> getAllDishes() {
        List<DishDTO> dishes = new ArrayList<>();
        MenuNode[] menu = restaurant.getMenu();
        String[] categories = restaurant.getCategoryArray();
        
        for (int i = 0; i < menu.length; i++) {
            MenuNode node = menu[i];
            while (node != null) {
                Dish dish = node.getDish();
                dishes.add(new DishDTO(
                    dish.getName(),
                    dish.getCategory(),
                    dish.getPriceOfDish(),
                    dish.getProfit()
                ));
                node = node.getNextMenuNode();
            }
        }
        return dishes;
    }

    public OrderResponse placeOrder(String dishName, int quantity) {
        boolean wasAvailable = restaurant.checkDishAvailability(dishName, quantity);
        restaurant.order(dishName, quantity);
        
        return new OrderResponse(
            dishName,
            quantity,
            wasAvailable,
            restaurant.profit()
        );
    }

    public StockResponse getStock(String ingredientName) {
        StockNode node = restaurant.findStockNode(ingredientName);
        if (node != null) {
            Ingredient ing = node.getIngredient();
            return new StockResponse(
                ing.getID(),
                ing.getName(),
                ing.getStockLevel(),
                ing.getCost()
            );
        }
        return null;
    }

    public void updateStock(String ingredientName, int amount) {
        restaurant.updateStock(ingredientName, -1, amount);
    }

    public double getCurrentProfit() {
        return restaurant.profit();
    }

    // Add more wrapper methods as needed
}
```

---

## Step 6: Create REST Controllers

**File: `src/main/java/com/ruhungry/controller/MenuController.java`**

```java
package com.ruhungry.controller;

import com.ruhungry.dto.DishDTO;
import com.ruhungry.service.RestaurantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/menu")
public class MenuController {

    @Autowired
    private RestaurantService restaurantService;

    @GetMapping
    public ResponseEntity<List<DishDTO>> getAllDishes() {
        return ResponseEntity.ok(restaurantService.getAllDishes());
    }

    @GetMapping("/categories")
    public ResponseEntity<String[]> getCategories() {
        // Implement wrapper method in service
        return ResponseEntity.ok(new String[]{"Appetizer", "Entree", "Dessert"});
    }
}
```

**File: `src/main/java/com/ruhungry/controller/OrderController.java`**

```java
package com.ruhungry.controller;

import com.ruhungry.dto.*;
import com.ruhungry.service.RestaurantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    @Autowired
    private RestaurantService restaurantService;

    @PostMapping
    public ResponseEntity<OrderResponse> placeOrder(@RequestBody OrderRequest request) {
        OrderResponse response = restaurantService.placeOrder(
            request.getDishName(),
            request.getQuantity()
        );
        return ResponseEntity.ok(response);
    }

    @GetMapping("/profit")
    public ResponseEntity<Double> getCurrentProfit() {
        return ResponseEntity.ok(restaurantService.getCurrentProfit());
    }
}
```

**File: `src/main/java/com/ruhungry/controller/InventoryController.java`**

```java
package com.ruhungry.controller;

import com.ruhungry.dto.*;
import com.ruhungry.service.RestaurantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/inventory")
public class InventoryController {

    @Autowired
    private RestaurantService restaurantService;

    @GetMapping("/{ingredientName}")
    public ResponseEntity<StockResponse> getStock(@PathVariable String ingredientName) {
        StockResponse stock = restaurantService.getStock(ingredientName);
        if (stock != null) {
            return ResponseEntity.ok(stock);
        }
        return ResponseEntity.notFound().build();
    }

    @PutMapping("/{ingredientName}")
    public ResponseEntity<Void> updateStock(
            @PathVariable String ingredientName,
            @RequestParam int amount) {
        restaurantService.updateStock(ingredientName, amount);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/restock")
    public ResponseEntity<Void> restock(@RequestBody RestockRequest request) {
        // Call your existing restock method
        return ResponseEntity.ok().build();
    }
}
```

---

## Step 7: Create DTOs (Data Transfer Objects)

**File: `src/main/java/com/ruhungry/dto/DishDTO.java`**

```java
package com.ruhungry.dto;

public class DishDTO {
    private String name;
    private String category;
    private double price;
    private double profit;

    public DishDTO(String name, String category, double price, double profit) {
        this.name = name;
        this.category = category;
        this.price = price;
        this.profit = profit;
    }

    // Getters and setters
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }
    public double getPrice() { return price; }
    public void setPrice(double price) { this.price = price; }
    public double getProfit() { return profit; }
    public void setProfit(double profit) { this.profit = profit; }
}
```

**File: `src/main/java/com/ruhungry/dto/OrderRequest.java`**

```java
package com.ruhungry.dto;

public class OrderRequest {
    private String dishName;
    private int quantity;

    // Getters and setters
    public String getDishName() { return dishName; }
    public void setDishName(String dishName) { this.dishName = dishName; }
    public int getQuantity() { return quantity; }
    public void setQuantity(int quantity) { this.quantity = quantity; }
}
```

**File: `src/main/java/com/ruhungry/dto/OrderResponse.java`**

```java
package com.ruhungry.dto;

public class OrderResponse {
    private String dishName;
    private int quantity;
    private boolean successful;
    private double currentProfit;

    public OrderResponse(String dishName, int quantity, boolean successful, double currentProfit) {
        this.dishName = dishName;
        this.quantity = quantity;
        this.successful = successful;
        this.currentProfit = currentProfit;
    }

    // Getters and setters
    public String getDishName() { return dishName; }
    public void setDishName(String dishName) { this.dishName = dishName; }
    public int getQuantity() { return quantity; }
    public void setQuantity(int quantity) { this.quantity = quantity; }
    public boolean isSuccessful() { return successful; }
    public void setSuccessful(boolean successful) { this.successful = successful; }
    public double getCurrentProfit() { return currentProfit; }
    public void setCurrentProfit(double currentProfit) { this.currentProfit = currentProfit; }
}
```

**File: `src/main/java/com/ruhungry/dto/StockResponse.java`**

```java
package com.ruhungry.dto;

public class StockResponse {
    private int id;
    private String name;
    private int stockLevel;
    private double cost;

    public StockResponse(int id, String name, int stockLevel, double cost) {
        this.id = id;
        this.name = name;
        this.stockLevel = stockLevel;
        this.cost = cost;
    }

    // Getters and setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public int getStockLevel() { return stockLevel; }
    public void setStockLevel(int stockLevel) { this.stockLevel = stockLevel; }
    public double getCost() { return cost; }
    public void setCost(double cost) { this.cost = cost; }
}
```

---

## Step 8: Configuration Files

**File: `src/main/resources/application.properties`**

```properties
# Server Configuration
server.port=8080
spring.application.name=RUHungry

# Logging
logging.level.com.ruhungry=DEBUG
logging.level.org.springframework.web=INFO

# Swagger/OpenAPI
springdoc.api-docs.path=/api-docs
springdoc.swagger-ui.path=/swagger-ui.html
```

---

## Step 9: Example API Endpoints

Once running, you'll have these REST endpoints:

```
GET    /api/menu                      - Get all dishes
GET    /api/menu/categories           - Get all categories
POST   /api/orders                    - Place an order
GET    /api/orders/profit             - Get current profit
GET    /api/inventory/{name}          - Get stock info
PUT    /api/inventory/{name}?amount=X - Update stock
POST   /api/inventory/restock         - Restock ingredients
```

---

## Step 10: Testing with cURL

```bash
# Start the application
mvn spring-boot:run

# Get all dishes
curl http://localhost:8080/api/menu

# Place an order
curl -X POST http://localhost:8080/api/orders \
  -H "Content-Type: application/json" \
  -d '{"dishName": "Hamburger", "quantity": 2}'

# Get current profit
curl http://localhost:8080/api/orders/profit

# Get stock level
curl http://localhost:8080/api/inventory/Lettuce

# Update stock
curl -X PUT "http://localhost:8080/api/inventory/Lettuce?amount=10"
```

---

## Step 11: Optional - Add Simple Frontend

**File: `src/main/resources/static/index.html`**

```html
<!DOCTYPE html>
<html>
<head>
    <title>RUHungry Dashboard</title>
    <style>
        body { font-family: Arial, sans-serif; margin: 20px; }
        .dish { border: 1px solid #ccc; padding: 10px; margin: 10px 0; }
        button { padding: 10px; margin: 5px; cursor: pointer; }
    </style>
</head>
<body>
    <h1>RUHungry Restaurant</h1>
    
    <div id="profit">
        <h2>Current Profit: $<span id="profitAmount">0.00</span></h2>
    </div>

    <div id="menu">
        <h2>Menu</h2>
        <div id="menuItems"></div>
    </div>

    <script>
        // Fetch menu
        fetch('/api/menu')
            .then(res => res.json())
            .then(dishes => {
                const menuDiv = document.getElementById('menuItems');
                dishes.forEach(dish => {
                    menuDiv.innerHTML += `
                        <div class="dish">
                            <strong>${dish.name}</strong> (${dish.category})<br>
                            Price: $${dish.price.toFixed(2)} | Profit: $${dish.profit.toFixed(2)}<br>
                            <button onclick="orderDish('${dish.name}', 1)">Order 1</button>
                        </div>
                    `;
                });
            });

        // Fetch profit
        function updateProfit() {
            fetch('/api/orders/profit')
                .then(res => res.json())
                .then(profit => {
                    document.getElementById('profitAmount').textContent = profit.toFixed(2);
                });
        }

        // Order dish
        function orderDish(dishName, quantity) {
            fetch('/api/orders', {
                method: 'POST',
                headers: {'Content-Type': 'application/json'},
                body: JSON.stringify({dishName, quantity})
            })
            .then(res => res.json())
            .then(response => {
                alert(`Order ${response.successful ? 'successful' : 'failed'}!`);
                updateProfit();
            });
        }

        updateProfit();
    </script>
</body>
</html>
```

---

## What Changes to Your Core Code?

### ✅ MINIMAL Changes Required:

1. **Replace `StdIn` with `Scanner` or `InputStream`**
   - Change file reading methods to accept InputStreams
   - Works with Spring Boot's resource loading

2. **Package your classes** 
   - Add `package com.ruhungry.core;`

3. **Move .in files to `resources/data/`**
   - Spring Boot uses classpath resources

### ✅ KEEPS Intact:

- ✅ All custom hashtable logic
- ✅ All linked list implementations
- ✅ All algorithms and data structures
- ✅ Order processing logic
- ✅ Inventory management
- ✅ Transaction tracking
- ✅ Everything else!

---

## Running the Project

```bash
# Create the project structure
mkdir ruhungry-springboot
cd ruhungry-springboot

# Or use Spring Initializr
# https://start.spring.io/

# Copy your core files to src/main/java/com/ruhungry/core/
# Copy .in files to src/main/resources/data/

# Run
mvn spring-boot:run

# Access
# API: http://localhost:8080/api/menu
# Swagger: http://localhost:8080/swagger-ui.html
# Frontend: http://localhost:8080/
```

---

## Key Benefits

1. ✅ **Keep all your custom data structures** - No rewrite needed!
2. ✅ **Add REST API layer** - Modern web interface
3. ✅ **Console app still works** - Can keep both versions
4. ✅ **Add to resume truthfully** - "Migrated to Spring Boot with REST APIs"
5. ✅ **Learn Spring Boot** - Real-world experience
6. ✅ **Impressive demo** - Show API calls in interviews

---

## Interview Talking Points

> "I built RUHungry initially as a console application with custom data structures, then wrapped it with Spring Boot REST APIs to make it web-accessible. The core hashtable and linked list implementations remained unchanged, demonstrating how solid OOP design allows for easy framework integration."

---

## Time Estimate

- **Basic REST API setup**: 2-3 hours
- **Full frontend**: +2-3 hours  
- **Testing & polish**: +2 hours

**Total**: 1 day to add Spring Boot wrapper around existing code!

---

## Summary

**You DON'T need to rewrite everything!**

Just:
1. Create new Spring Boot project
2. Copy your existing core files (95% unchanged)
3. Change `StdIn` to `InputStream` (small tweak)
4. Add REST controllers (new files)
5. Add service layer (wraps your logic)
6. Done!

Your custom hashtables, linked lists, and algorithms stay exactly the same. You're just adding a web API layer on top.
