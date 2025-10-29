# RUHungry Spring Boot - Command Reference

## 🚀 Running the Application

### Start the Server
```bash
cd c:\Users\hitan\Downloads\RUHungry\ruhungry-springboot
mvn spring-boot:run
```

### Build JAR (for deployment)
```bash
mvn clean package
java -jar target/ruhungry-springboot-1.0.0.jar
```

### Clean Build
```bash
mvn clean install
```

---

## 🌐 Access URLs

| Resource | URL |
|----------|-----|
| **Web Dashboard** | http://localhost:8080 |
| **Swagger UI** | http://localhost:8080/swagger-ui.html |
| **API Docs JSON** | http://localhost:8080/api-docs |

---

## 🧪 cURL Testing Commands

### Menu Operations

**Get all dishes:**
```bash
curl http://localhost:8080/api/menu
```

**Get categories:**
```bash
curl http://localhost:8080/api/menu/categories
```

**Get dishes by category:**
```bash
curl http://localhost:8080/api/menu/category/Appetizer
curl http://localhost:8080/api/menu/category/Entree
curl http://localhost:8080/api/menu/category/Dessert
```

### Order Operations

**Place an order:**
```bash
curl -X POST http://localhost:8080/api/orders \
  -H "Content-Type: application/json" \
  -d "{\"dishName\":\"Hamburger\",\"quantity\":2}"
```

**Get current profit:**
```bash
curl http://localhost:8080/api/orders/profit
```

### Inventory Operations

**Get all ingredients:**
```bash
curl http://localhost:8080/api/inventory
```

**Get specific ingredient stock:**
```bash
curl http://localhost:8080/api/inventory/Lettuce
```

**Update stock manually:**
```bash
curl -X PUT "http://localhost:8080/api/inventory/Lettuce?amount=10"
```

**Restock ingredient:**
```bash
curl -X POST http://localhost:8080/api/inventory/restock \
  -H "Content-Type: application/json" \
  -d "{\"ingredientName\":\"Lettuce\",\"quantity\":50}"
```

**Donate ingredient:**
```bash
curl -X POST http://localhost:8080/api/inventory/donate \
  -H "Content-Type: application/json" \
  -d "{\"ingredientName\":\"Lettuce\",\"quantity\":10}"
```

---

## 🔧 Development Commands

### Hot Reload (with DevTools)
Just edit files and save - changes auto-reload! ⚡

### Check Java Version
```bash
java -version
```
(Need Java 17+)

### Check Maven Version
```bash
mvn -version
```
(Need Maven 3.6+)

### Run Tests (when you add them)
```bash
mvn test
```

### Generate JAR without tests
```bash
mvn clean package -DskipTests
```

---

## 📊 PowerShell Commands (Windows)

### Copy data files
```powershell
Copy-Item "c:\Users\hitan\Downloads\RUHungry\RUHungry\*.in" `
  -Destination "c:\Users\hitan\Downloads\RUHungry\ruhungry-springboot\src\main\resources\data\" `
  -Force
```

### View project structure
```powershell
tree /F ruhungry-springboot
```

### Open in browser
```powershell
start http://localhost:8080
```

---

## 🐛 Troubleshooting Commands

### Port in use? Check what's using port 8080
```powershell
netstat -ano | findstr :8080
```

### Kill process on port (replace PID)
```powershell
taskkill /PID <PID> /F
```

### Change port
Edit `src/main/resources/application.properties`:
```properties
server.port=8081
```

### Clean Maven cache
```bash
mvn clean
```

### View logs
Logs appear in console where you ran `mvn spring-boot:run`

---

## 📦 Git Commands (for version control)

### Initialize repo
```bash
cd ruhungry-springboot
git init
git add .
git commit -m "Initial Spring Boot migration"
```

### Create GitHub repo and push
```bash
git remote add origin <your-repo-url>
git branch -M main
git push -u origin main
```

---

## 🎯 Testing Scenarios

### Scenario 1: Happy Path Order
```bash
# Check menu
curl http://localhost:8080/api/menu

# Check profit (should be 0)
curl http://localhost:8080/api/orders/profit

# Place order
curl -X POST http://localhost:8080/api/orders \
  -H "Content-Type: application/json" \
  -d "{\"dishName\":\"Hamburger\",\"quantity\":2}"

# Check profit again (should be > 0)
curl http://localhost:8080/api/orders/profit
```

### Scenario 2: Check Inventory
```bash
# List all ingredients
curl http://localhost:8080/api/inventory

# Check specific ingredient
curl http://localhost:8080/api/inventory/Lettuce

# Restock if low
curl -X POST http://localhost:8080/api/inventory/restock \
  -H "Content-Type: application/json" \
  -d "{\"ingredientName\":\"Lettuce\",\"quantity\":100}"
```

### Scenario 3: Browse by Category
```bash
# Get all categories
curl http://localhost:8080/api/menu/categories

# Get appetizers
curl http://localhost:8080/api/menu/category/Appetizer

# Get entrees  
curl http://localhost:8080/api/menu/category/Entree

# Get desserts
curl http://localhost:8080/api/menu/category/Dessert
```

---

## 🎨 Browser Testing

### Interactive Testing
1. Open http://localhost:8080
2. Click category tabs to filter
3. Click "Order" buttons to place orders
4. Watch profit update in real-time
5. Scroll down to see inventory

### API Documentation Testing
1. Open http://localhost:8080/swagger-ui.html
2. Click any endpoint
3. Click "Try it out"
4. Fill in parameters
5. Click "Execute"
6. View response

---

## 📝 Quick Reference

| Want to... | Command |
|------------|---------|
| **Start app** | `mvn spring-boot:run` |
| **View UI** | Open http://localhost:8080 |
| **View API docs** | Open http://localhost:8080/swagger-ui.html |
| **Get menu** | `curl http://localhost:8080/api/menu` |
| **Place order** | See "Place an order" above |
| **Check profit** | `curl http://localhost:8080/api/orders/profit` |
| **Stop app** | Ctrl+C in terminal |
| **Build JAR** | `mvn clean package` |

---

## 🚀 Production Deployment

### Build production JAR
```bash
mvn clean package -DskipTests
```

### Run production JAR
```bash
java -jar target/ruhungry-springboot-1.0.0.jar
```

### Deploy to Heroku (example)
```bash
heroku create ruhungry-app
git push heroku main
heroku open
```

---

## 💡 Pro Tips

1. **Keep the terminal open** - Watch logs for debugging
2. **Use Swagger UI** - Easier than writing cURL commands
3. **Check browser console** - F12 for frontend errors
4. **Hot reload works** - Edit & save, no restart needed
5. **Test in order** - Menu → Order → Profit → Inventory

---

**Happy coding! 🎉**
