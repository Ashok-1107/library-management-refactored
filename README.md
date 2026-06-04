# Library Management System — SOLID Refactoring

A Java project demonstrating refactoring of a legacy "God Class" codebase into a clean, maintainable architecture using **SOLID principles** and **design patterns**, with full **JUnit 5** test coverage.

---

## Project Structure

```
src/
├── main/java/
│   ├── before/
│   │   └── LibraryManager.java        ← Original bad code (all violations documented)
│   └── after/
│       ├── model/
│       │   ├── Book.java
│       │   ├── Member.java            ← Abstract base
│       │   ├── StandardMember.java
│       │   ├── PremiumMember.java
│       │   ├── StudentMember.java
│       │   └── BorrowRecord.java
│       ├── repository/
│       │   ├── BookRepository.java        ← Interface
│       │   ├── InMemoryBookRepository.java
│       │   ├── MemberRepository.java      ← Interface
│       │   └── InMemoryMemberRepository.java
│       ├── notification/
│       │   ├── NotificationService.java       ← Interface
│       │   ├── EmailNotificationService.java
│       │   └── SmsNotificationService.java
│       └── service/
│           ├── BookService.java
│           ├── MemberService.java
│           └── BorrowService.java
└── test/java/after/
    ├── BookServiceTest.java       ← 6 tests
    ├── MemberServiceTest.java     ← 7 tests
    └── BorrowServiceTest.java     ← 10 tests
```

---

## Running the Tests

```bash
# Clone the repo
git clone https://github.com/Ashok-1107/library-management-refactored.git
cd library-management-refactored

# Run all tests
mvn test
```

Expected output: **23 tests, all passing**

---

## Refactoring Report

### Scenario

The original codebase had a single `LibraryManager` class — a classic **God Class** — responsible for everything: managing books, members, borrow/return logic, fine calculation, notifications, and report generation. This is a very common pattern in legacy Java backends.

---

### Problems Found (Before)

#### 1. Single Responsibility Principle (SRP) — VIOLATED
**File:** `before/LibraryManager.java`

The `LibraryManager` class had 5+ distinct responsibilities in one file:
- Book CRUD operations
- Member registration
- Borrow/return logic
- Fine calculation
- Email notifications
- Report printing

**Why it's a problem:** Any change to email logic risks breaking borrow logic. Any change to reporting affects book management. The class becomes impossible to maintain as it grows.

---

#### 2. Open/Closed Principle (OCP) — VIOLATED
**File:** `before/LibraryManager.java` — `registerMember()` and `borrowBook()`

Both methods had long `if-else` chains checking member type:
```java
// Before — adding a new member type = editing existing code
if (type.equals("PREMIUM")) { limit = 10; }
else if (type.equals("STUDENT")) { limit = 2; }
else if (type.equals("STANDARD")) { limit = 3; }
// Adding STAFF? Edit this method. And this one. And this one.
```

**Why it's a problem:** Every new member type requires editing multiple methods. This introduces regression risk every time.

---

#### 3. Liskov Substitution Principle (LSP) — VIOLATED
**File:** `before/LibraryManager.java` — `PremiumMember` inner logic

The original code had member-specific behavior scattered across the manager class rather than in proper subclasses. If a subclass overrode behavior unexpectedly, callers would break.

---

#### 4. Interface Segregation Principle (ISP) — VIOLATED
**File:** `before/LibraryManager.java`

No interfaces were used at all. Everything was one concrete class. Consumers were forced to take the entire `LibraryManager` even if they only needed one capability.

---

#### 5. Dependency Inversion Principle (DIP) — VIOLATED
**File:** `before/LibraryManager.java`

```java
// Before — hard-coded concrete dependency
private EmailNotifier emailNotifier = new EmailNotifier();
```

The `LibraryManager` was directly instantiating `EmailNotifier`. You could not:
- Swap it for SMS without editing the class
- Mock it in unit tests
- Change notification behavior at runtime

---

### Fixes Applied (After)

| Violation | Fix Applied |
|---|---|
| SRP — God Class | Split into `BookService`, `MemberService`, `BorrowService`, repository classes, and model classes |
| OCP — if-else member types | Abstract `Member` class with `getBorrowLimit()` — new types add a new subclass, zero existing code changes |
| LSP — broken subclass behavior | Each member subclass (`StandardMember`, `PremiumMember`, `StudentMember`) fully honors the `Member` contract |
| ISP — no interfaces | `BookRepository`, `MemberRepository`, `NotificationService` interfaces introduced |
| DIP — hard-coded EmailNotifier | `NotificationService` interface injected via constructor — swap Email for SMS with zero service code changes |

---

### Design Patterns Applied

| Pattern | Where Used | Why |
|---|---|---|
| **Strategy Pattern** | `NotificationService` interface | Swap email/SMS notification at runtime |
| **Repository Pattern** | `BookRepository`, `MemberRepository` | Decouple data storage from business logic |
| **Template Method** | Abstract `Member` class | Common structure, subclass-specific limits |
| **Dependency Injection** | All service constructors | Enables testing and flexibility |

---

### Improvements Observed

| Metric | Before | After |
|---|---|---|
| Classes | 1 (God Class) | 14 focused classes |
| Interfaces | 0 | 3 |
| Unit testable components | 0 (tightly coupled) | All services fully testable |
| Adding new member type | Edit 3+ methods | Add 1 new class |
| Adding new notification channel | Edit `LibraryManager` | Add 1 new class |
| Test coverage | 0% | 23 tests, all passing |

---

### Key Takeaway

The refactored code is **open for extension but closed for modification**. Adding a new member type (`StaffMember`), a new notification channel (`PushNotificationService`), or a new data store (`DatabaseBookRepository`) requires **zero changes to existing tested code** — just new classes implementing existing interfaces.

---

*Refactored by Ashok Sravan Gajjala*  
*Stack: Java 17, JUnit 5, Mockito, Maven*  
*Date: June 2026*
