# URL Shortener System

A backend URL-shortener application built with Spring Boot. It supports custom aliases, URL activation/deactivation, redirects, JWT authentication, role-based authorization, and resource ownership checks.

> React frontend integration is the next phase.

## Features

- Create shortened URLs
- Create custom URL aliases
- Redirect short URLs to their original destination
- Activate and deactivate links
- Register and log in users
- JWT-based stateless authentication
- Role-based authorization: `USER` and `ADMIN`
- Ownership authorization: users can access or modify only their own URLs
- Admin dashboard APIs for all users and URLs
- Global exception handling and consistent API response format
- PostgreSQL persistence

## Tech Stack

- Java 21
- Spring Boot 3
- Spring Security
- Spring Data JPA
- PostgreSQL
- JWT (`jjwt`)
- Maven
- Lombok
- Springdoc OpenAPI / Swagger

## Architecture

```text
Client / React Frontend
        |
        v
Controller Layer
        |
        v
Service Layer
        |
        v
Repository Layer
        |
        v
PostgreSQL
```

Authentication flow:

```text
Login request
  → UserAuthService verifies BCrypt password hash
  → JwtService creates JWT with userId and role
  → Client sends Authorization: Bearer <token>
  → JwtAuthenticationFilter validates JWT
  → Spring SecurityContext stores authenticated identity
  → Controller and service authorize the request
```

## Security and Authorization

The application uses stateless JWT authentication.

- Public registration and login endpoints do not require a token.
- Protected requests must include `Authorization: Bearer <JWT>`.
- The backend derives the current user from the JWT; it does not trust a `userId` sent by the client.
- `USER` can access and modify only their own URL records.
- `ADMIN` can access and modify all users and URL records.

| Endpoint | USER | ADMIN |
|---|---:|---:|
| `POST /v2/url` | Create own URL | Create own URL |
| `GET /v2/url/me` | Own URLs | Own URLs |
| `GET /v2/url` | Forbidden | All URLs |
| `GET /v2/url/{id}` | Owner only | Any URL |
| `PUT /v2/url/{id}` | Owner only | Any URL |
| Activate/deactivate URL | Owner only | Any URL |
| `GET /v2/user/me` | Own profile | Own profile |
| `GET /v2/user` | Forbidden | All users |

> Important: public registration always assign the `USER` role on the server. # URL Shortener System

A backend URL-shortener application built with Spring Boot. It supports custom aliases, URL activation/deactivation, redirects, JWT authentication, role-based authorization, and resource ownership checks.

> React frontend integration is the next phase.

## Features

- Create shortened URLs
- Create custom URL aliases
- Redirect short URLs to their original destination
- Activate and deactivate links
- Register and log in users
- JWT-based stateless authentication
- Role-based authorization: `USER` and `ADMIN`
- Ownership authorization: users can access or modify only their own URLs
- Admin dashboard APIs for all users and URLs
- Global exception handling and consistent API response format
- PostgreSQL persistence

## Tech Stack

- Java 21
- Spring Boot 3
- Spring Security
- Spring Data JPA
- PostgreSQL
- JWT (`jjwt`)
- Maven
- Lombok
- Springdoc OpenAPI / Swagger

## Architecture

```text
Client / React Frontend
        |
        v
Controller Layer
        |
        v
Service Layer
        |
        v
Repository Layer
        |
        v
PostgreSQL
```

Authentication flow:

```text
Login request
  → UserAuthService verifies BCrypt password hash
  → JwtService creates JWT with userId and role
  → Client sends Authorization: Bearer <token>
  → JwtAuthenticationFilter validates JWT
  → Spring SecurityContext stores authenticated identity
  → Controller and service authorize the request
```

## Security and Authorization

The application uses stateless JWT authentication.

- Public registration and login endpoints do not require a token.
- Protected requests must include `Authorization: Bearer <JWT>`.
- The backend derives the current user from the JWT; it does not trust a `userId` sent by the client.
- `USER` can access and modify only their own URL records.
- `ADMIN` can access and modify all users and URL records.

| Endpoint | USER | ADMIN |
|---|---:|---:|
| `POST /v2/url` | Create own URL | Create own URL |
| `GET /v2/url/me` | Own URLs | Own URLs |
| `GET /v2/url` | Forbidden | All URLs |
| `GET /v2/url/{id}` | Owner only | Any URL |
| `PUT /v2/url/{id}` | Owner only | Any URL |
| Activate/deactivate URL | Owner only | Any URL |
| `GET /v2/user/me` | Own profile | Own profile |
| `GET /v2/user` | Forbidden | All users |

> Important: public registration should always assign the `USER` role on the server to not allow a client to register itself as `ADMIN`.

## API Endpoints

### Authentication

| Method | Endpoint | Description |
|---|---|---|
| `POST` | `/v2/user/register` | Register a user |
| `POST` | `/v2/user/login/userid` | Login using user ID |
| `POST` | `/v2/user/login/email` | Login using email |
| `GET` | `/v2/user/me` | Fetch current user profile |
| `GET` | `/v2/user` | Fetch all users — ADMIN only |

### URL Management

| Method | Endpoint | Description |
|---|---|---|
| `POST` | `/v2/url` | Create a short URL |
| `GET` | `/v2/url/me` | Fetch current user's URLs |
| `GET` | `/v2/url` | Fetch all URLs — ADMIN only |
| `GET` | `/v2/url/{id}` | Fetch a URL by ID |
| `PUT` | `/v2/url/{id}` | Update a URL |
| `PUT` | `/v2/url/activate/{id}` | Activate a URL |
| `PUT` | `/v2/url/deactivate/{id}` | Deactivate a URL |
| `GET` | `/{shortCode}` | Redirect to original URL |

## Example: Login

```http
POST /v2/user/login/email
Content-Type: application/json
```

```json
{
  "email": "user@example.com",
  "password": "Password@123"
}
```

Successful login returns an access token:

```json
{
  "success": true,
  "data": {
    "userId": "shreya01",
    "role": "USER",
    "accessToken": "eyJ..."
  }
}
```

Use it for protected endpoints:

```http
Authorization: Bearer eyJ...
```

## Local Setup

### Prerequisites

- Java 21
- Maven
- PostgreSQL

### Configure local properties

Create or update `application-local.yaml` with your PostgreSQL configuration and JWT settings.

Never commit real database passwords or JWT secrets to GitHub.

### Run the application

```bash
cd backend
./mvnw spring-boot:run
```

On Windows:

```powershell
.\mvnw.cmd spring-boot:run
```

## API Response Format

```json
{
  "success": true,
  "respCode": "URL_CREATED",
  "respDescription": "Short URL created successfully",
  "data": {},
  "timeStamp": "2026-09-03T10:00:00"
}
```

## Future Enhancements

- React + TypeScript frontend
- Refresh-token authentication
- Logout token revocation / blacklist
- URL click analytics
- Pagination and search
- Rate limiting
- Docker Compose setup
- Unit and integration tests
- Deployment pipeline

## Project Documentation

- [High-Level Design](docs/hld.md)
- [Low-Level Design](docs/lld.md)
- [API Contract](docs/api-contract.md)
- [Database Schema](docs/db-schema.md)Do not allow a client to register itself as `ADMIN`.

## API Endpoints

### Authentication

| Method | Endpoint | Description |
|---|---|---|
| `POST` | `/v2/user/register` | Register a user |
| `POST` | `/v2/user/login/userid` | Login using user ID |
| `POST` | `/v2/user/login/email` | Login using email |
| `GET` | `/v2/user/me` | Fetch current user profile |
| `GET` | `/v2/user` | Fetch all users — ADMIN only |

### URL Management

| Method | Endpoint | Description |
|---|---|---|
| `POST` | `/v2/url` | Create a short URL |
| `GET` | `/v2/url/me` | Fetch current user's URLs |
| `GET` | `/v2/url` | Fetch all URLs — ADMIN only |
| `GET` | `/v2/url/{id}` | Fetch a URL by ID |
| `PUT` | `/v2/url/{id}` | Update a URL |
| `PUT` | `/v2/url/activate/{id}` | Activate a URL |
| `PUT` | `/v2/url/deactivate/{id}` | Deactivate a URL |
| `GET` | `/{shortCode}` | Redirect to original URL |

## Example: Login

```http
POST /v2/user/login/email
Content-Type: application/json
```

```json
{
  "email": "user@example.com",
  "password": "Password@123"
}
```

Successful login returns an access token:

```json
{
  "success": true,
  "data": {
    "userId": "shreya01",
    "role": "USER",
    "accessToken": "eyJ..."
  }
}
```

Use it for protected endpoints:

```http
Authorization: Bearer eyJ...
```

## Local Setup

### Prerequisites

- Java 21
- Maven
- PostgreSQL

### Configure local properties

Create or update `application-local.yaml` with your PostgreSQL configuration and JWT settings.

Never commit real database passwords or JWT secrets to GitHub.

### Run the application

```bash
cd backend
./mvnw spring-boot:run
```

On Windows:

```powershell
.\mvnw.cmd spring-boot:run
```

## API Response Format

```json
{
  "success": true,
  "respCode": "URL_CREATED",
  "respDescription": "Short URL created successfully",
  "data": {},
  "timeStamp": "2026-09-03T10:00:00"
}
```

## Future Enhancements

- React + TypeScript frontend
- Refresh-token authentication
- Logout token revocation / blacklist
- URL click analytics
- Pagination and search
- Rate limiting
- Docker Compose setup
- Unit and integration tests
- Deployment pipeline

## Project Documentation

- [High-Level Design](docs/hld.md)
- [Low-Level Design](docs/lld.md)
- [API Contract](docs/api-contract.md)
- [Database Schema](docs/db-schema.md)
