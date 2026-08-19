# Clean Architecture Spring Boot

Demo project implementing **Clean Architecture** with Spring Boot and Oracle DB.

## 📚 Documentation

All architecture details, structure, and setup instructions: **[ARCHITECTURE.md](ARCHITECTURE.md)**

## 🚀 Quick Start

```bash
# 1. Setup Oracle DB (see database/schema.sql)
# 2. Configure application.yml
# 3. Run
mvn spring-boot:run

# 4. Test
curl -X POST http://localhost:8080/api/users \
  -H "Content-Type: application/json" \
  -d '{"email":"test@test.com","fullName":"Test User"}'
```

## 📁 Structure

```
domain/          → Core business logic (Layer 1)
application/     → Use cases (Layer 2)
infrastructure/  → Technical implementation (Layer 3)
```

See **[ARCHITECTURE.md](ARCHITECTURE.md)** for complete details.
