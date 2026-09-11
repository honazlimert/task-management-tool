# Task Management Tool (TMT)



A Jira-like **Task and Project Management REST API** with JWT-based authentication and role-based authorization. Built with Spring Boot 4 following a layered architecture (controller–service–repository).


---

## Table of Contents

- [Features](#features)
- [Tech Stack](#tech-stack)
- [Architecture](#architecture)
- [Data Model](#data-model)
- [Getting Started](#getting-started)
  - [Prerequisites](#prerequisites)
  - [Installation](#installation)
  - [Environment Variables](#environment-variables)
  - [Running with Docker](#running-with-docker)
  - [Running Locally (Maven)](#running-locally-maven)
- [API Documentation](#api-documentation)
- [API Endpoints](#api-endpoints)
- [Authentication & Authorization](#authentication--authorization)
- [Business Rules](#business-rules)
- [Scheduled Job](#scheduled-job)
- [Error Handling](#error-handling)
- [Testing](#testing)
- [Project Structure](#project-structure)
- [Roadmap](#roadmap)
- [Contributing](#contributing)
- [License](#license)

---

## Features

- 🔐 **JWT-based authentication** with `ADMIN` / `USER` role-based endpoint authorization (`@PreAuthorize`)
- 📋 **Project and task management**: create, list, filter, update, delete
- 🔄 **Task status workflow** (`TODO → IN_PROGRESS → DONE`) with a full status-change history (audit log)
- 👤 **Task assignment** (assignee) and user management
- 🧵 **Optimistic locking** (`@Version`) to prevent concurrent update conflicts
- ⏰ **Scheduled job**: a daily cron that detects and logs stale tasks (no status change in 7 days)
- ✅ **Bean Validation** plus a custom `@ValidStoryPoint` validator (Fibonacci story points: 1, 2, 3, 5, 8, 13)
- 📄 **Pagination and dynamic filtering** (status, priority, project, assignee) for task listing
- 📚 **OpenAPI / Swagger UI** for interactive API documentation
- 🐳 **Docker & Docker Compose** support for one-command startup
- 🧪 **JUnit 5 + Mockito** unit tests for the service layer

## Tech Stack

| Layer | Technology |
|---|---|
| Language / Platform | Java 25 |
| Framework | Spring Boot 4.1.0 (Web MVC, Data JPA, Validation, Security) |
| Database | PostgreSQL 15 |
| Authentication | JWT ([jjwt](https://github.com/jwtk/jjwt) 0.12.6), BCrypt |
| API Documentation | springdoc-openapi (Swagger UI) 2.8.4 |
| Build Tool | Maven (via Maven Wrapper) |
| Testing | JUnit 5, Mockito |
| Code Generation | Lombok |
| Containerization | Docker, Docker Compose |

## Architecture

The project follows a classic layered architecture: **controller → service → repository → entity**. Requests first pass through `JwtAuthenticationFilter`, then through role-based `@PreAuthorize` checks before reaching the controller. DTOs (`request` / `response`) never leak entities directly; conversion is handled by classes in the `mapper` package. Errors are caught centrally by `GlobalExceptionHandler` (`@RestControllerAdvice`) and converted into a consistent JSON format.

```
Client
  │  HTTP + Bearer JWT
  ▼
JwtAuthenticationFilter  ──▶  SecurityContext
  │
  ▼
Controller (Auth / Task / User / Project)
  │  DTO (request)
  ▼
Service (business rules, @Transactional)
  │
  ├──▶ Mapper ──▶ Entity
  └──▶ Repository (Spring Data JPA)
           │
           ▼
       PostgreSQL
```



## Data Model

| Entity | Description | Relationships |
|---|---|---|
| `User` | System user (email, password, role) | 1–N with `Task.assignee` |
| `Project` | Project (name, description) | 1–N with `Task` |
| `Task` | Task (title, status, priority, story point) | N–1 with `Project`, N–1 with `User` (assignee) |
| `TaskHistory` | Task status change record (audit log) | references `Task.id` |

**Enums:** `Role (USER, ADMIN)` · `Status (TODO, IN_PROGRESS, DONE)` · `Priority (LOW, MEDIUM, HIGH)`

## Getting Started

### Prerequisites

- JDK 25+
- Maven (or the bundled `mvnw` / `mvnw.cmd`)
- PostgreSQL 15+ (if not using Docker)
- Docker & Docker Compose *(optional, recommended)*

### Installation

```bash
git clone https://github.com/<your-username>/internship-project-tmt.git
cd internship-project-tmt
```

### Environment Variables

Copy the `.env.example` file in the project root and fill in your own values:

```bash
cp .env.example .env
```

| Variable | Description | Example |
|---|---|---|
| `DB_NAME` | PostgreSQL database name | `tmt_db` |
| `DB_USERNAME` | Database username | `postgres` |
| `DB_PASSWORD` | Database password | `change-me` |
| `JWT_SECRET` | JWT signing key (Base64, at least 256-bit) | `generate-a-strong-256-bit-secret` |

> ⚠️ The `.env` file is listed in `internship-project-tmt/.gitignore` and **must never be committed**. Replace `JWT_SECRET` and `DB_PASSWORD` with strong, randomly generated values in real environments.

### Running with Docker

The fastest way to get started; spins up both the application and PostgreSQL with a single command:

```bash
docker compose up --build
```

The application will be available at `http://localhost:8080`, and the database on port `5433` (used to avoid conflicts with a local PostgreSQL instance).

### Running Locally (Maven)

```bash
# Make sure PostgreSQL is running on localhost:5432
./mvnw spring-boot:run
```

To run the tests:

```bash
./mvnw test
```

## API Documentation

Once the application is running, explore and test all endpoints through the interactive Swagger UI:

- Swagger UI: `http://localhost:8080/swagger-ui.html`
- OpenAPI JSON: `http://localhost:8080/v3/api-docs`

Click **Authorize** in the top-right corner of Swagger UI and paste the JWT you received from `/api/auth/login` in the `Bearer <token>` format to make authorized requests.

## API Endpoints

### Auth — `/api/auth` *(public)*

| Method | Endpoint | Description |
|---|---|---|
| `POST` | `/api/auth/register` | Registers a new user (default role: `USER`) |
| `POST` | `/api/auth/login` | Logs in and returns a JWT |

### Task — `/api/tasks`

| Method | Endpoint | Access | Description |
|---|---|---|---|
| `POST` | `/api/tasks` | `ADMIN`, `USER` | Creates a new task (default status: `TODO`) |
| `GET` | `/api/tasks` | `ADMIN`, `USER` | Paginated, filterable task list (`status`, `priority`, `projectId`, `assigneeId`, `page`, `size`) |
| `GET` | `/api/tasks/{id}` | `ADMIN`, `USER` | Fetches a single task |
| `PUT` | `/api/tasks/{id}` | `ADMIN`, `USER` | Updates a task |
| `DELETE` | `/api/tasks/{id}` | `ADMIN` | Deletes a task |
| `PATCH` | `/api/tasks/{id}/status` | `ADMIN`, `USER` | Changes task status (creates an audit log entry) |
| `PATCH` | `/api/tasks/{id}/assignee` | `ADMIN`, `USER` | Changes the task's assignee |

### Project — `/api/projects`

| Method | Endpoint | Access | Description |
|---|---|---|---|
| `POST` | `/api/projects` | `ADMIN` | Creates a new project |
| `GET` | `/api/projects` | `ADMIN`, `USER` | Lists all projects |
| `GET` | `/api/projects/{id}` | `ADMIN`, `USER` | Fetches a single project |
| `DELETE` | `/api/projects/{id}` | `ADMIN` | Deletes a project |

### User — `/api/users`

| Method | Endpoint | Access | Description |
|---|---|---|---|
| `POST` | `/api/users` | `ADMIN` | Creates a new user (with a specified role) |
| `GET` | `/api/users` | `ADMIN` | Lists all users |
| `GET` | `/api/users/{id}` | `ADMIN` | Fetches a single user |
| `DELETE` | `/api/users/{id}` | `ADMIN` | Deletes a user |

## Authentication & Authorization

1. `/api/auth/register` and `/api/auth/login` are the only endpoints left `permitAll` in `SecurityConfig`; every other endpoint requires authentication (`STATELESS` session policy).
2. After login, the returned JWT must be sent on subsequent requests via the `Authorization: Bearer <token>` header.
3. `JwtAuthenticationFilter` validates the token, extracts the user's email, looks up the user in the database, and writes their role to the `SecurityContext` as `ROLE_<ROLE>`.
4. Controller methods are protected at the method level using `@PreAuthorize("hasRole('ADMIN')")` / `@PreAuthorize("hasAnyRole('ADMIN', 'USER')")`.
5. `SecurityAuditorAware` supplies the logged-in user's email to populate `@CreatedBy` fields (e.g. `Project.createdBy`, `Task.createdBy`).

## Business Rules

- **Story Point**: Only Fibonacci values are accepted → `1, 2, 3, 5, 8, 13` (`@ValidStoryPoint` / `StoryPointValidator`).
- **Task status transitions**:
  - Setting the same status again throws `InvalidTaskStatusException`.
  - A task in `DONE` status can **never** be moved back to another status.
  - Every successful status change is recorded in `TaskHistory` with `oldStatus`, `newStatus`, `changedBy`, and `changedDate`.
- **Optimistic Locking**: The `@Version` field on `Task` returns a `409 Conflict` when a concurrent update conflict occurs.
- **Registration**: The same email cannot be used to register more than once; passwords are hashed with `BCrypt` before being stored.

## Scheduled Job

`TaskSchedulerService.checkStaleTasks()` runs every day at midnight (`cron = "0 0 0 * * *"`):

1. Finds task IDs whose most recent status change is older than 7 days, via `TaskHistoryRepository`.
2. Filters to only `TODO` or `IN_PROGRESS` tasks (excluding `DONE`).
3. Logs the resulting "stale" tasks at `WARN`/`INFO` level.

> This feature is enabled via the `@EnableScheduling` annotation on `InternshipProjectTmtApplication`.

## Error Handling

All errors are converted into a consistent JSON format by `GlobalExceptionHandler` (`@RestControllerAdvice`):

```json
{
  "message": "Task not found!",
  "status": 404,
  "timestamp": "2026-09-11T12:00:00"
}
```

| Exception | HTTP Status | Trigger |
|---|---|---|
| `TaskNotFoundException` / `ProjectNotFoundException` / `UserNotFoundException` | 404 | Record not found |
| `BusinessException` / `InvalidTaskStatusException` | 400 | Business rule violation |
| `MethodArgumentNotValidException` / `ConstraintViolationException` | 400 | `@Valid` / Bean Validation failure |
| `HttpMessageNotReadableException` | 400 | Malformed JSON body |
| `ObjectOptimisticLockingFailureException` | 409 | Concurrent update conflict |
| `AccessDeniedException` | 403 | Unauthorized access |
| `Exception` (fallback) | 500 | Unexpected server error |

## Testing

The service layer is unit-tested in isolation using Mockito to mock repository/mapper dependencies:

- `AuthServiceTest` — register/login scenarios, duplicate email, wrong password
- `TaskServiceTest` — task creation, status transitions, `DONE → TODO` guard, history logging
- `UserServiceTest`, `ProjectServiceTest` — CRUD and "not found" scenarios
- `TaskSchedulerServiceTest` — stale task filtering logic
- `JwtServiceTest` — token generation and verification

```bash
./mvnw test
```

## Project Structure

```
src/main/java/com/atmosware/internship_project_tmt/
├── config/          # SecurityConfig, JwtAuthenticationFilter, SwaggerConfig, SecurityAuditorAware
├── controller/       # AuthController, TaskController, ProjectController, UserController
├── service/          # AuthService, TaskService, ProjectService, UserService, JwtService, TaskSchedulerService
├── repository/        # Spring Data JPA repository interfaces
├── entity/            # User, Task, Project, TaskHistory + enums (Role, Status, Priority)
├── dto/
│   ├── request/        # Incoming request DTOs
│   └── response/       # Outgoing response DTOs
├── mapper/             # Entity ↔ DTO conversion classes
├── exception/          # Custom exception classes + GlobalExceptionHandler
└── validation/         # @ValidStoryPoint + StoryPointValidator
```
