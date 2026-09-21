# Clean Architecture - Spring Boot Project

## 📐 Kiến trúc tổng quan

Project này implement **Clean Architecture** theo cách đặt tên phổ biến trong Spring community: **Domain-Application-Infrastructure** (3 layers thực tế = gộp từ 4 layers lý thuyết).

### Clean Architecture - 4 Layers Gốc (Uncle Bob)
```
┌─────────────────────────────────────────────────┐
│  Layer 4: Frameworks & Drivers                 │ ← Web, DB, Devices
│  ┌──────────────────────────────────────────┐  │
│  │  Layer 3: Interface Adapters             │  │ ← Controllers, Gateways
│  │  ┌────────────────────────────────────┐  │  │
│  │  │  Layer 2: Use Cases                │  │  │ ← Application logic
│  │  │  ┌──────────────────────────────┐  │  │  │
│  │  │  │  Layer 1: Entities           │  │  │  │ ← Business rules
│  │  │  └──────────────────────────────┘  │  │  │
│  │  └────────────────────────────────────┘  │  │
│  └──────────────────────────────────────────┘  │
└─────────────────────────────────────────────────┘
```

### Implementation này - 3 Layers (Gộp Layer 3+4)
```
┌─────────────────────────────────────────────────────────┐
│        Infrastructure Layer (Layer 3 + 4 gộp)          │
│  ┌─ Web (Controllers, DTOs)                            │
│  ├─ Persistence (JPA, Adapters)                        │
│  └─ Config (Spring Boot, Beans)                        │
│                                                         │
│  ┌───────────────────────────────────────────────┐     │
│  │         Application Layer (Layer 2)           │     │
│  │  (Use Cases, Ports, Commands, Results)        │     │
│  │                                                │     │
│  │  ┌─────────────────────────────────────────┐  │     │
│  │  │      Domain Layer (Layer 1)             │  │     │
│  │  │  (Entities, Value Objects, Rules)       │  │     │
│  │  │                                         │  │     │
│  │  └─────────────────────────────────────────┘  │     │
│  └───────────────────────────────────────────────┘     │
└─────────────────────────────────────────────────────────┘

Dependencies: Infrastructure → Application → Domain
```

### Dependency Rule
```
Infrastructure ──→ Application ──→ Domain
   (Layer 3+4)      (Layer 2)       (Layer 1)
```

### 💡 Tại sao gộp Layer 3 & 4?

**Lý thuyết Uncle Bob (4 layers):**
- Layer 3: Interface Adapters (Controllers, Gateways, Presenters)
- Layer 4: Frameworks & Drivers (Spring, JPA, Web framework)

**Thực tế (3 layers):**
- Infrastructure = Layer 3 + Layer 4 gộp lại
- Vì trong Spring Boot, Controllers và Framework config đều dùng Spring annotations
- Không cần tách riêng vì đều phụ thuộc framework
- Đơn giản hơn, dễ organize code hơn

**Vẫn đúng nguyên tắc Clean Architecture:**
- ✅ Domain không phụ thuộc gì
- ✅ Application chỉ phụ thuộc Domain
- ✅ Infrastructure phụ thuộc cả Domain và Application
- ✅ Dependency Rule vẫn được tuân thủ (hướng vào trong)

---

## ❓ Câu hỏi thường gặp: Sao chỉ có 3 layers?

### Clean Architecture gốc có 4 layers mà?

**Đúng!** Clean Architecture của Uncle Bob có **4 layers đồng tâm:**

1. **Entities** (Yellow) - Enterprise Business Rules
2. **Use Cases** (Red) - Application Business Rules  
3. **Interface Adapters** (Green) - Controllers, Gateways, Presenters
4. **Frameworks & Drivers** (Blue) - Web, DB, UI, Devices

### Vậy project này thiếu layer?

**KHÔNG thiếu!** Project này **GỘP Layer 3 & 4 lại thành Infrastructure**.

#### Lý do gộp:

**Layer 3 (Interface Adapters):**
- Controllers → dùng `@RestController`, `@PostMapping` (Spring)
- Repository Adapters → dùng `@Component`, inject JPA
- Đã phụ thuộc Spring framework

**Layer 4 (Frameworks & Drivers):**
- Spring Boot configs → `@Configuration`, `@Bean`
- JPA setup → `@EnableJpaRepositories`
- Database configs

**➡️ Cả 2 layers đều cần Spring framework**  
**➡️ Đều là "technical infrastructure"**  
**➡️ Hợp lý khi gộp thành 1 package: `infrastructure/`**

#### So sánh:

| Approach | Packages | Phân chia |
|----------|----------|-----------|
| **Lý thuyết (4 layers)** | `entities/`, `usecases/`, `interfaceadapters/`, `frameworks/` | Tách bạch hoàn toàn |
| **Thực tế (3 layers)** | `domain/`, `application/`, `infrastructure/` | Gộp layer 3+4 |

#### Vẫn đúng Clean Architecture?

**✅ CÓ!** Vì:
- Domain vẫn không phụ thuộc gì (pure Java)
- Application chỉ phụ thuộc Domain
- Infrastructure phụ thuộc cả Domain và Application
- **Dependency Rule vẫn được tuân thủ: Infrastructure → Application → Domain**

#### Khi nào cần tách Layer 3 & 4?

Cân nhắc tách khi:
- Project rất lớn (100+ developers)
- Muốn build riêng adapters và frameworks
- Có nhiều framework implementations khác nhau
- Cần enforce cứng boundary giữa adapter và framework

**Với hầu hết projects:** Gộp lại đơn giản và đủ dùng.

---

## 🔌 Tại sao gọi là "Port"?

### Nguồn gốc: Hexagonal Architecture (Ports & Adapters)

**Port** là thuật ngữ từ **Hexagonal Architecture** của Alistair Cockburn (2005), còn gọi là **Ports & Adapters Pattern**.

#### Ví dụ thực tế (Hardware)

```
┌──────────────┐        ┌──────────────┐
│   Computer   │        │  USB Device  │
│              │        │              │
│  ┌────────┐  │        │              │
│  │  CPU   │  │        │              │
│  │ (Core) │  │        │              │
│  └────────┘  │        │              │
│      ║       │        │              │
│  ┌────────┐  │  USB   │              │
│  │  PORT  │◄─┼────────┼─►  ADAPTER  │
│  └────────┘  │  Cable │              │
│              │        │              │
└──────────────┘        └──────────────┘
```

- **PORT** (cổng): Interface trên computer (USB port, HDMI port)
- **ADAPTER**: Device kết nối vào port (USB cable, HDMI cable)
- **Core**: CPU, không biết gì về external devices

#### Áp dụng vào Software

```
┌────────────────────────┐        ┌──────────────────┐
│   Application Core     │        │  External System │
│                        │        │                  │
│  ┌──────────────────┐  │        │                  │
│  │   Use Case       │  │        │                  │
│  │  (Business Logic)│  │        │                  │
│  └──────────────────┘  │        │                  │
│          ║             │        │                  │
│  ┌──────────────────┐  │        │                  │
│  │      PORT        │◄─┼────────┼─►    ADAPTER    │
│  │   (Interface)    │  │        │ (Implementation) │
│  └──────────────────┘  │        │                  │
│                        │        │                  │
└────────────────────────┘        └──────────────────┘
```

---

### Port = Interface (Cổng giao tiếp)

**Port** là **interface** định nghĩa contract giữa:
- Application core (bên trong)
- External systems (bên ngoài)

#### Đặc điểm của Port:

1. **Là interface** (không phải implementation)
2. **Định nghĩa bởi core** (không phải external)
3. **Stable** (ít thay đổi)
4. **Abstract** (không biết implementation)

---

### 2 loại Port

#### **Input Port (Primary/Driving Port)**
- **Ai gọi:** External actors (User, Controller) gọi vào core
- **Mục đích:** Cho phép bên ngoài **SỬ DỤNG** core
- **Ví dụ:** Use case interfaces, Commands

```
Controller ──(calls)──> Input Port ──> Use Case
```

**Trong project này:**
```java
// application/port/in/CreateUserCommand.java
public class CreateUserCommand {  // Input Port
    private final String email;
    private final String fullName;
}

// Controller (external) sends command INTO application
```

#### **Output Port (Secondary/Driven Port)**
- **Ai gọi:** Core gọi ra external systems
- **Mục đích:** Core **CẦN** services từ bên ngoài
- **Ví dụ:** Repository interfaces, External service interfaces

```
Use Case ──(calls)──> Output Port ──(implemented by)──> Adapter ──> Database
```

**Trong project này:**
```java
// application/port/out/UserRepositoryPort.java
public interface UserRepositoryPort {  // Output Port
    User save(User user);
    Optional<User> findByEmail(Email email);
}

// Use Case calls this port
// Adapter (infrastructure) implements this port
```

---

### Port vs Adapter

| | Port | Adapter |
|---|------|---------|
| **Là gì?** | Interface | Implementation |
| **Ở đâu?** | Application layer | Infrastructure layer |
| **Ai định nghĩa?** | Application (core) | Infrastructure (external) |
| **Phụ thuộc?** | Không phụ thuộc gì | Phụ thuộc port |
| **Ví dụ** | `UserRepositoryPort` | `UserRepositoryAdapter` |

---

### Tại sao cần Port?

#### ❌ Không có Port (Tight coupling)
```java
public class CreateUserUseCase {
    private UserJpaRepository jpaRepo;  // ❌ Phụ thuộc JPA
    
    public void execute() {
        UserJpaEntity entity = ...;     // ❌ Phụ thuộc JPA entity
        jpaRepo.save(entity);
    }
}
```

**Vấn đề:**
- Use case phụ thuộc JPA
- Không thể test mà không có database
- Không thể đổi database dễ dàng

#### ✅ Có Port (Loose coupling)
```java
public class CreateUserUseCase {
    private UserRepositoryPort port;    // ✅ Phụ thuộc interface
    
    public void execute() {
        User user = ...;                // ✅ Domain object
        port.save(user);
    }
}
```

**Lợi ích:**
- Use case chỉ phụ thuộc interface
- Dễ test (mock port)
- Dễ đổi implementation (Oracle → Postgres)

---

### Trong project này

#### Output Port (Repository)
```
Application Layer:
  application/port/out/UserRepositoryPort.java  ← Port (interface)
        ▲
        │ implements
        │
Infrastructure Layer:
  infrastructure/persistence/adapter/UserRepositoryAdapter.java  ← Adapter
```

#### Input Port (Command)
```
Infrastructure Layer:
  UserController  ──creates──>  CreateUserCommand  ──sends to──>  UseCase
                                      ▲
                                      │
                                   Input Port
                            (application/port/in/)
```

---

### Tóm tắt

**"Port" = Cổng giao tiếp (Interface)**

- Giống như **USB port** trên máy tính
- Định nghĩa **contract** giữa core và external
- Cho phép **plug & play** different adapters
- **Core không biết** implementation cụ thể là gì

**Port trong Hexagonal Architecture = Interface trong Clean Architecture**

Cả hai đều nhằm **Dependency Inversion**: Core định nghĩa interface, external implement.

---

## 📁 Cấu trúc Project

```
src/main/java/com/cleanarch/
│
├── 📦 domain/                          ═══ LAYER 1: DOMAIN ═══
│   │
│   ├── model/                          Domain Entities (Pure Java)
│   │   └── User.java                   - Business entity
│   │                                   - Business rules & validation
│   │                                   - NO framework dependencies
│   │
│   ├── valueobject/                    Value Objects
│   │   ├── Email.java                  - Immutable objects
│   │   └── UserId.java                 - Self-validating
│   │                                   - Business constraints
│   │
│   └── port/                           Domain Ports
│       └── UserRepository.java         - Repository interface
│                                       - Defined by domain needs
│
├── 📦 application/                     ═══ LAYER 2: APPLICATION ═══
│   │
│   ├── usecase/                        Use Cases (Business Logic)
│   │   ├── CreateUserUseCase.java      - Application-specific logic
│   │   └── UserAlreadyExistsException.java - Orchestrates domain
│   │
│   └── port/                           Application Ports
│       ├── in/                         Input Ports (Commands & Results)
│       │   ├── CreateUserCommand.java  - Input DTO
│       │   └── CreateUserResult.java   - Output DTO
│       │
│       └── out/                        Output Ports (Interfaces)
│           └── UserRepositoryPort.java - Repository contract
│
└── 📦 infrastructure/                  ═══ LAYER 3: INFRASTRUCTURE ═══
    │
    ├── web/                            Web Adapters (REST API)
    │   ├── controller/
    │   │   └── UserController.java     - REST endpoints
    │   │                               - HTTP request handling
    │   │
    │   └── dto/                        Web DTOs
    │       ├── CreateUserRequest.java  - HTTP request body
    │       └── UserResponse.java       - HTTP response body
    │
    ├── persistence/                    Database Adapters
    │   ├── adapter/
    │   │   └── UserRepositoryAdapter.java - Implements port
    │   │                                  - Domain ↔ JPA mapping
    │   │
    │   ├── entity/
    │   │   └── UserJpaEntity.java      - JPA entity (@Entity)
    │   │                               - Database mapping
    │   │
    │   └── repository/
    │       └── UserJpaRepository.java  - Spring Data JPA
    │                                   - Database operations
    │
    ├── messaging/                      Kafka Adapters (Event-Driven)
    │   ├── publisher/
    │   │   └── KafkaPublisher.java     - Generic Kafka publisher
    │   │                               - Async message sending
    │   │
    │   ├── consumer/
    │   │   └── KafkaConsumer.java      - Generic Kafka consumer
    │   │                               - Event listeners
    │   │
    │   ├── event/
    │   │   ├── UserCreatedEvent.java   - User created event DTO
    │   │   └── UserUpdatedEvent.java   - User updated event DTO
    │   │
    │   └── service/
    │       └── UserEventPublisher.java - User event publisher service
    │                                   - Business event publishing
    │
    └── config/                         Configuration
        ├── CleanArchitectureApplication.java - Spring Boot main
        ├── UseCaseConfiguration.java         - Bean wiring
        ├── KafkaConfiguration.java           - Kafka setup
        └── GlobalExceptionHandler.java       - Exception handling
```

---

## 🔄 Request Flow (POST /api/users)

```
1️⃣  HTTP Request
    POST /api/users
    Body: {"email": "user@example.com", "fullName": "User Name"}
        │
        ▼
2️⃣  UserController (Infrastructure/Web)
    - Validates HTTP request
    - Converts CreateUserRequest → CreateUserCommand
        │
        ▼
3️⃣  CreateUserUseCase (Application)
    - Receives CreateUserCommand
    - Creates Email value object (validates format)
    - Checks existsByEmail() via UserRepositoryPort
    - If exists → throws UserAlreadyExistsException
        │
        ▼
4️⃣  User Entity (Domain)
    - new User(email, fullName)
    - Validates fullName (business rule: >= 2 chars)
    - Sets id = UUID, createdAt = now(), active = true
        │
        ▼
5️⃣  Back to CreateUserUseCase
    - Calls userRepositoryPort.save(user)
        │
        ▼
6️⃣  UserRepositoryAdapter (Infrastructure/Persistence)
    - Converts User (domain) → UserJpaEntity
    - Calls jpaRepository.save(jpaEntity)
        │
        ▼
7️⃣  UserJpaRepository (Infrastructure/Persistence)
    - Spring Data JPA
    - Executes INSERT statement
        │
        ▼
8️⃣  Oracle Database
    - Persists data to USERS table
        │
        ▼
9️⃣  Back through layers
    - JPA Entity → Domain User → CreateUserResult
        │
        ▼
🔟  UserController
    - Converts CreateUserResult → UserResponse
    - Returns HTTP 201 Created + JSON body
```

---

## 🎯 Mapping với Clean Architecture

### Uncle Bob's 4 Layers → Project này (3 Layers)

| Uncle Bob (4 Layers) | Project Layer | Package | Ví dụ | Note |
|---------------------|---------------|---------|-------|------|
| **Layer 1: Entities** | **Domain** | `domain/model/` | `User.java` | Pure business |
| (Enterprise Rules) | | `domain/valueobject/` | `Email.java` | No framework |
| | | `domain/port/` | `UserRepository.java` | Domain needs |
| **Layer 2: Use Cases** | **Application** | `application/usecase/` | `CreateUserUseCase.java` | Orchestrate |
| (Application Rules) | | `application/port/in/` | `CreateUserCommand.java` | Input |
| | | `application/port/out/` | `UserRepositoryPort.java` | Output port |
| **Layer 3: Interface Adapters** | **Infrastructure** | `infrastructure/web/` | `UserController.java` | **GỘP** |
| (Controllers, Gateways) | | `infrastructure/persistence/` | `UserRepositoryAdapter.java` | với |
| + | + | + | + | Layer 4 |
| **Layer 4: Frameworks** | **Infrastructure** | `infrastructure/config/` | `CleanArchitectureApplication` | **↓** |
| (Spring, DB, Devices) | | `infrastructure/persistence/entity/` | `UserJpaEntity.java` | Thành 1 |

### Tại sao gộp Layer 3 & 4?

**4 Layers (Lý thuyết):**
```
Frameworks & Drivers    ← Spring Boot, JPA, Oracle driver
Interface Adapters      ← Controllers, Repository adapters  
Use Cases               ← Business logic
Entities                ← Core domain
```

**3 Layers (Thực tế):**
```
Infrastructure          ← Controllers + Spring + JPA (gộp layer 3+4)
Application             ← Business logic (layer 2)
Domain                  ← Core domain (layer 1)
```

**Lý do:**
1. **Controllers dùng Spring annotations** → cần Spring framework
2. **Repository adapters dùng JPA** → cần framework
3. **Cả 2 đều là "technical details"** → gộp chung Infrastructure
4. **Easier to organize** → 3 packages thay vì 4
5. **Still respects Dependency Rule** → Infrastructure không được vào Domain

---

## 📊 Dependency Flow

### Correct (Clean Architecture)
```
┌──────────────┐      ┌─────────────┐      ┌──────────┐
│ UserContr... │ ───> │ CreateUser  │ ───> │  User    │
│              │      │  UseCase    │      │          │
│ (Infra/Web)  │      │             │      │ (Domain) │
└──────────────┘      │ (App)       │      └──────────┘
                      │             │           ▲
                      │    uses     │           │
                      │      │      │           │
                      │      ▼      │           │
                      │ ┌─────────┐ │           │
                      │ │  Port   │─┼───────────┘
                      │ └─────────┘ │
                      └─────────────┘
                             ▲
                             │ implements
                             │
                    ┌────────┴─────────┐
                    │ RepoAdapter      │
                    │ (Infra/Persist)  │
                    └──────────────────┘
```

### Key Points:
- ✅ Domain không import gì từ Application hay Infrastructure
- ✅ Application chỉ import từ Domain
- ✅ Infrastructure import từ cả Domain và Application
- ✅ Dependency hướng VÀO TRONG

---

## 🔑 Các thành phần chi tiết

### Layer 1: Domain (Core Business Logic)

#### **Domain Entity: User.java**
```java
package com.cleanarch.domain.model;

// Pure Java - NO framework dependencies
public class User {
    private final UserId id;
    private final Email email;
    private final String fullName;
    private final LocalDateTime createdAt;
    private boolean active;

    // Business Rule: Validate full name
    private String validateFullName(String fullName) {
        if (fullName.trim().length() < 2) {
            throw new IllegalArgumentException("Full name >= 2 chars");
        }
        return fullName.trim();
    }

    // Business Rule: Deactivate user
    public void deactivate() {
        if (!this.active) {
            throw new IllegalStateException("Already inactive");
        }
        this.active = false;
    }
}
```

**Đặc điểm:**
- ✅ Pure Java (no @Entity, @Column)
- ✅ Business logic ở đây
- ✅ Self-validating
- ✅ Rich domain model (not anemic)

---

#### **Value Object: Email.java**
```java
package com.cleanarch.domain.valueobject;

public class Email {
    private static final Pattern EMAIL_PATTERN = "...";
    private final String value;

    public Email(String value) {
        if (!EMAIL_PATTERN.matcher(value).matches()) {
            throw new IllegalArgumentException("Invalid email");
        }
        this.value = value.toLowerCase();
    }
}
```

**Đặc điểm:**
- ✅ Immutable
- ✅ Self-validating
- ✅ Encapsulates business rules

---

### Layer 2: Application (Use Cases)

#### **Use Case: CreateUserUseCase.java**
```java
package com.cleanarch.application.usecase;

public class CreateUserUseCase {
    private final UserRepositoryPort userRepository;

    public CreateUserResult execute(CreateUserCommand command) {
        // 1. Validate
        Email email = new Email(command.getEmail());
        
        // 2. Business rule: unique email
        if (userRepository.existsByEmail(email)) {
            throw new UserAlreadyExistsException(...);
        }

        // 3. Create domain entity
        User user = new User(email, command.getFullName());

        // 4. Save via port
        User saved = userRepository.save(user);

        // 5. Return result
        return new CreateUserResult(...);
    }
}
```

**Đặc điểm:**
- ✅ Orchestrates domain objects
- ✅ Application-specific logic
- ✅ Uses ports (interfaces)
- ✅ No framework dependencies

---

#### **Input Port: CreateUserCommand.java**
```java
package com.cleanarch.application.port.in;

public class CreateUserCommand {
    private final String email;
    private final String fullName;
    // constructor, getters
}
```

**Đặc điểm:**
- ✅ Simple DTO
- ✅ Immutable
- ✅ Input to use case

---

#### **Output Port: UserRepositoryPort.java**
```java
package com.cleanarch.application.port.out;

public interface UserRepositoryPort {
    User save(User user);
    Optional<User> findByEmail(Email email);
    boolean existsByEmail(Email email);
}
```

**Đặc điểm:**
- ✅ Interface defined by application needs
- ✅ Works with domain objects (User, Email)
- ✅ Will be implemented by infrastructure

---

### Layer 3: Infrastructure (Technical Details)

#### **Web Controller: UserController.java**
```java
package com.cleanarch.infrastructure.web.controller;

@RestController
@RequestMapping("/api/users")
public class UserController {
    private final CreateUserUseCase createUserUseCase;

    @PostMapping
    public ResponseEntity<UserResponse> createUser(
            @RequestBody CreateUserRequest request) {
        
        // Convert Web DTO → Command
        CreateUserCommand command = new CreateUserCommand(
            request.getEmail(),
            request.getFullName()
        );

        // Execute use case
        CreateUserResult result = createUserUseCase.execute(command);

        // Convert Result → Web DTO
        UserResponse response = new UserResponse(...);
        
        return ResponseEntity.status(201).body(response);
    }
}
```

**Đặc điểm:**
- ✅ Spring annotations allowed here
- ✅ Converts between web DTOs and commands
- ✅ Calls use case

---

#### **Persistence Adapter: UserRepositoryAdapter.java**
```java
package com.cleanarch.infrastructure.persistence.adapter;

@Component
public class UserRepositoryAdapter implements UserRepositoryPort {
    private final UserJpaRepository jpaRepository;

    @Override
    public User save(User user) {
        // Domain → JPA
        UserJpaEntity entity = toJpaEntity(user);
        
        // Save
        UserJpaEntity saved = jpaRepository.save(entity);
        
        // JPA → Domain
        return toDomainUser(saved);
    }

    private UserJpaEntity toJpaEntity(User user) { ... }
    private User toDomainUser(UserJpaEntity entity) { ... }
}
```

**Đặc điểm:**
- ✅ Implements port from application
- ✅ Handles domain ↔ JPA mapping
- ✅ Isolates JPA from domain

---

#### **JPA Entity: UserJpaEntity.java**
```java
package com.cleanarch.infrastructure.persistence.entity;

@Entity
@Table(name = "USERS")
public class UserJpaEntity {
    @Id
    private String id;
    
    @Column(nullable = false, unique = true)
    private String email;
    
    @Column(nullable = false)
    private String fullName;
    
    @Column(nullable = false)
    private LocalDateTime createdAt;
    
    @Column(nullable = false)
    private boolean active;
    
    // getters/setters
}
```

**Đặc điểm:**
- ✅ JPA annotations allowed here
- ✅ Separate from domain User
- ✅ Database mapping only

---

## 🎯 Key Design Decisions

### 1. Domain Entity ≠ JPA Entity

**Why?**
- Domain entity: Business logic, rich model
- JPA entity: Database mapping, anemic

**Benefit:**
- Freedom to change domain without affecting DB
- Freedom to change DB without affecting domain

---

### 2. Ports & Adapters Pattern

**Ports (Interfaces):**
- `UserRepositoryPort` - defined by application
- `CreateUserCommand` - input structure
- `CreateUserResult` - output structure

**Adapters (Implementations):**
- `UserRepositoryAdapter` - implements repository port
- `UserController` - adapts HTTP to use case
- `UserJpaRepository` - Spring Data implementation

**Benefit:**
- Easy to mock for testing
- Easy to swap implementations
- Decoupled from frameworks

---

### 3. No Input/Output Boundaries (Simplified)

**Uncle Bob style:**
```
Controller → InputBoundary → UseCase → OutputBoundary → Presenter
```

**This project (Simplified):**
```
Controller → UseCase → Controller
```

**Why simplified?**
- For simple CRUD, boundaries add overhead
- Controller can handle conversion
- Still maintains dependency rule
- Pragmatic for small projects

**When to add boundaries?**
- Multiple presenters (Web + CLI + GraphQL)
- Complex response formatting
- Large enterprise projects

---

## 🧪 Testing Strategy

### Unit Test Domain
```java
@Test
void shouldThrowException_whenFullNameTooShort() {
    Email email = new Email("test@test.com");
    
    assertThrows(IllegalArgumentException.class, 
        () -> new User(email, "A"));
}
```

### Unit Test Use Case
```java
@Test
void shouldThrowException_whenEmailAlreadyExists() {
    // Mock repository
    when(repository.existsByEmail(any())).thenReturn(true);
    
    CreateUserCommand command = new CreateUserCommand(...);
    
    assertThrows(UserAlreadyExistsException.class,
        () -> useCase.execute(command));
}
```

### Integration Test Controller
```java
@SpringBootTest
@AutoConfigureMockMvc
class UserControllerTest {
    @Test
    void shouldCreateUser() {
        mockMvc.perform(post("/api/users")
            .contentType(APPLICATION_JSON)
            .content("{...}"))
            .andExpect(status().isCreated());
    }
}
```

---

## 📋 Business Rules

### Domain Layer (User.java)
- ✅ Full name >= 2 characters
- ✅ User created as active by default
- ✅ Can activate/deactivate

### Domain Layer (Email.java)
- ✅ Must match email regex pattern
- ✅ Auto lowercase and trim

### Application Layer (CreateUserUseCase.java)
- ✅ Email must be unique (cannot duplicate)

---

## 🚀 Setup & Run

### 1. Database Setup
```sql
-- Run database/schema.sql
CREATE TABLE USERS (
    ID VARCHAR2(50) PRIMARY KEY,
    EMAIL VARCHAR2(255) NOT NULL UNIQUE,
    FULL_NAME VARCHAR2(255) NOT NULL,
    CREATED_AT TIMESTAMP NOT NULL,
    ACTIVE NUMBER(1) NOT NULL
);
```

### 2. Configuration
```yaml
# src/main/resources/application.yml
spring:
  datasource:
    url: jdbc:oracle:thin:@localhost:1521:ORCL
    username: your_username
    password: your_password
```

### 3. Build & Run
```bash
mvn clean install
mvn spring-boot:run
```

### 4. Test API
```bash
curl -X POST http://localhost:8080/api/users \
  -H "Content-Type: application/json" \
  -d '{
    "email": "test@example.com",
    "fullName": "Test User"
  }'
```

**Success Response (201 Created):**
```json
{
  "id": "550e8400-e29b-41d4-a716-446655440000",
  "email": "test@example.com",
  "fullName": "Test User",
  "createdAt": "2026-08-20T00:00:00",
  "status": "ACTIVE"
}
```

---

## 💡 Benefits of This Architecture

### ✅ Testability
- Unit test domain without framework
- Unit test use cases with mocks
- Easy to test business logic

### ✅ Independence
- Business logic independent of:
  - Framework (Spring)
  - Database (Oracle)
  - UI (REST API)
- Can swap any without affecting others

### ✅ Flexibility
- Easy to add new use cases
- Easy to add new adapters (GraphQL, gRPC)
- Easy to change database

### ✅ Maintainability
- Clear separation of concerns
- Each layer has single responsibility
- Easy to understand and navigate

### ✅ Scalability
- Can split into microservices later
- Can add caching at adapter level
- Can add message queues easily

---

## 📚 References

- [Clean Architecture - Robert C. Martin (Uncle Bob)](https://blog.cleancoder.com/uncle-bob/2012/08/13/the-clean-architecture.html)
- [Hexagonal Architecture - Alistair Cockburn](https://alistair.cockburn.us/hexagonal-architecture/)
- [Spring Boot Documentation](https://spring.io/projects/spring-boot)
- [Domain-Driven Design - Eric Evans](https://domainlanguage.com/ddd/)
- [Apache Kafka Documentation](https://kafka.apache.org/documentation/)
- [Spring Kafka Documentation](https://spring.io/projects/spring-kafka)

---

## 📡 Event-Driven Architecture với Kafka

### Tại sao dùng Kafka?

**Kafka** cho phép xây dựng **Event-Driven Architecture**, giúp:
- ✅ **Decoupling**: Services không phụ thuộc trực tiếp vào nhau
- ✅ **Scalability**: Xử lý hàng triệu events/giây
- ✅ **Reliability**: Message không bị mất nhờ persistence
- ✅ **Asynchronous**: Xử lý bất đồng bộ, tăng performance
- ✅ **Event Sourcing**: Lưu trữ toàn bộ history của events

### Kafka trong Clean Architecture

```
┌─────────────────────────────────────────────────┐
│            Infrastructure Layer                  │
│                                                  │
│  ┌──────────────┐         ┌─────────────────┐  │
│  │  Controller  │         │  KafkaConsumer  │  │
│  └──────┬───────┘         └────────┬────────┘  │
│         │                          │            │
│         │ trigger                  │ listen     │
│         ▼                          ▼            │
│  ┌──────────────────────────────────────────┐  │
│  │       UserEventPublisher Service         │  │
│  │  (publishes events to Kafka)             │  │
│  └──────────────┬───────────────────────────┘  │
│                 │                               │
│                 │ publish                       │
│                 ▼                               │
│  ┌──────────────────────────────────────────┐  │
│  │         KafkaPublisher                   │  │
│  │  (generic Kafka adapter)                 │  │
│  └──────────────┬───────────────────────────┘  │
│                 │                               │
└─────────────────┼───────────────────────────────┘
                  │
                  ▼
        ┌─────────────────┐
        │  Kafka Cluster  │
        │  (External)     │
        └─────────────────┘
```

### Event Flow Example

```
1️⃣  User creates account (POST /api/users)
        │
        ▼
2️⃣  UserController receives request
        │
        ▼
3️⃣  CreateUserUseCase executes
        │
        ▼
4️⃣  User saved to database
        │
        ▼
5️⃣  UserEventPublisher.publishUserCreated()
        │
        ▼
6️⃣  Event sent to Kafka topics:
        - user-created
        - user-events
        │
        ▼
7️⃣  Other services consume events:
        - Email Service → Send welcome email
        - Analytics Service → Track new user
        - Notification Service → Push notification
        - Audit Service → Log user creation
```

### Kafka Components

#### 1. Configuration (`KafkaConfiguration.java`)
- Producer factory
- Consumer factory
- Listener container factory
- Serialization/deserialization setup

#### 2. Publisher (`KafkaPublisher.java`)
- Generic publisher for any message type
- Async and sync publishing
- Error handling and logging

#### 3. Event Publisher (`UserEventPublisher.java`)
- Business-specific event publisher
- Publishes UserCreatedEvent, UserUpdatedEvent
- Uses KafkaPublisher internally

#### 4. Consumer (`KafkaConsumer.java`)
- Listens to Kafka topics
- Manual acknowledgment
- Error handling
- Routes to business logic

#### 5. Event Models
- `UserCreatedEvent.java` - User creation event
- `UserUpdatedEvent.java` - User update event
- JSON serializable POJOs

### Topics Strategy

| Topic | Purpose | Producers | Consumers |
|-------|---------|-----------|-----------|
| `user-events` | All user events | UserEventPublisher | Multiple services |
| `user-created` | User creation | UserEventPublisher | Email, Analytics |
| `user-updated` | User updates | UserEventPublisher | Cache, Sync |
| `order-events` | Order events | OrderEventPublisher | Inventory, Shipping |

### Setup Kafka

#### Quick Start với Docker:

```bash
# Start Kafka with Zookeeper
docker-compose -f docker-compose-kafka.yml up -d

# Check status
docker ps

# Access Kafka UI
http://localhost:8090
```

#### Thêm chi tiết: Xem [KAFKA-SETUP.md](KAFKA-SETUP.md)

### Integration Example

#### Publishing Events từ Use Case:

```java
public class CreateUserUseCase {
    private final UserRepositoryPort userRepository;
    private final UserEventPublisher eventPublisher;
    
    public CreateUserResult execute(CreateUserCommand command) {
        // Create and save user
        User user = new User(email, fullName);
        User saved = userRepository.save(user);
        
        // Publish event
        eventPublisher.publishUserCreated(
            saved.getId().getValue(),
            saved.getEmail().getValue(),
            saved.getFullName(),
            saved.getCreatedAt(),
            saved.isActive()
        );
        
        return new CreateUserResult(saved);
    }
}
```

#### Consuming Events:

```java
@KafkaListener(topics = "user-created", groupId = "email-service")
public void onUserCreated(ConsumerRecord<String, UserCreatedEvent> record) {
    UserCreatedEvent event = record.value();
    
    // Send welcome email
    emailService.sendWelcomeEmail(event.getEmail(), event.getFullName());
    
    // Acknowledge
    ack.acknowledge();
}
```

### Benefits

1. **Loose Coupling**: Services không cần biết về nhau
2. **Scalability**: Thêm consumers dễ dàng
3. **Reliability**: Kafka persistence đảm bảo không mất message
4. **Audit Trail**: Events = history log
5. **Multiple Consumers**: Một event, nhiều xử lý

### Best Practices

1. ✅ **Idempotent Consumers**: Xử lý duplicate messages
2. ✅ **Schema Evolution**: Version events khi thay đổi
3. ✅ **Error Handling**: Dead letter queue cho failed messages
4. ✅ **Monitoring**: Track lag, throughput, errors
5. ✅ **Testing**: Test với embedded Kafka

---

## 🎓 Summary

**This project demonstrates:**
- ✅ Clean Architecture principles
- ✅ Domain-Application-Infrastructure pattern
- ✅ Ports & Adapters (Hexagonal Architecture)
- ✅ Dependency Inversion Principle
- ✅ Separation of domain and persistence
- ✅ Industry-standard Spring Boot structure

**Key takeaway:** 
> Dependencies always point INWARD. Domain is the center, infrastructure is the outer layer.
