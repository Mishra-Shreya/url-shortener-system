src/main/java/com/urlshortener/backend
|
|-- BackendApplication.java
|
|-- common
|   |-- exception
|   |   |-- GlobalExceptionHandler.java //spring exception handling 
|   |   |-- UrlShortenerException.java //spring exception handling
|   |
|   |-- response
|   |   |-- ApiResponse.java
|   |   |-- ApiResponseBuilder.java //builder pattern
|   |   |-- ResponseCode.java //enum
|   |   |-- ResponseType.java //enum
|
|-- url
|   |-- controller
|   |   |-- UrlController.java
|   |
|   |-- service
|   |   |-- validator
|   |   |   |-- UrlValidator.java
|   |   |   |-- IUrlValidator.java
|   |   |
|   |   |-- UrlService.java
|   |
|   |-- repository //spring data jpa //repository design pattern
|   |   |-- UrlRepository.java
|   |   |-- CustomUrlRepository.java
|   |
|   |-- entity //spring data jpa
|   |   |-- Url.java
|   |   |-- CustomUrl.java
|   |   |-- UrlStatus.java //enum //future
|   |
|   |-- dto
|   |   |-- service
|   |   |   |-- DtoService.java
|   |   |
|   |   |-- request
|   |   |   |-- UrlRequestDto.java //spring validation
|   |   |   |-- UpdateUrlRequest.java
|   |   |
|   |   |-- response
|   |       |-- UrlResponseDto.java
|
|-- redirect
|   |-- controller
|   |   |-- RedirectController.java
|   |
|   |-- service
|   |   |-- RedirectService.java
|
|-- utility
|   |-- service
|   |   |-- SequenceService.java
|   |
|   |-- IShortCodeGenerator.java
|   |-- ShortCodeGenerator.java
|   |-- ShortCodeGenerationStrategy.java //strategy design pattern //future
|   |-- Base62ShortCodeGenerator.java //strategy design pattern //future
|
|-- appuser
|   |-- controller
|   |   |-- UserAuthController.java
|   |
|   |-- service
|   |   |-- UserAuthService.java
|   |   |-- validator
|   |       |-- UserValidator.java
|   |       |-- IUserValidator.java
|   |
|   |-- repository
|   |   |-- UserRepository.java
|   |
|   |-- entity
|   |   |-- User.java
|   |
|   |-- dto
|   |   |-- request
|   |   |   |-- RegisterRequestDto.java
|   |   |   |-- LoginRequestDto.java
|   |   |   |-- EmailLoginRequestDto.java
|   |   |-- response
|   |   |       |-- ResponseDto.java
|   |
|   |-- security
|       |-- SecurityConfig.java
|       |-- JwtService.java
|       |-- JwtAuthenticationFilter.java



Authentication APIs
POST /v2/user/register
POST /v2/user/login/userid
POST /v2/user/login/email
POST /v2/user/logout

URL APIs
POST /v2/url                         authenticated; owner derived from JWT
GET  /v2/url/me                      authenticated; returns current user's URLs
GET  /v2/url                         ADMIN only; returns all URLs
GET  /v2/url/{id}                    owner or ADMIN
PUT  /v2/url/{id}                    owner or ADMIN
PUT  /v2/url/activate/{id}           owner or ADMIN
PUT  /v2/url/deactivate/{id}         owner or ADMIN
GET  /v2/url/custom/{customCode}     ADMIN only

Redirect API
GET /{shortCode}                     public


common contains reusable project-wide things:
common/exception
common/response

url contains URL management:
create URL
update URL
activate/deactivate URL
fetch user URLs
fetch URL by id
fetch URLs by customCode
validations

redirect contains redirect flow:
GET /{shortCode}
resolve original URL
increase click count
redirect

shortcode contains ID/short code generation logic. 
This deserves its own package because it is an important design piece.



Custom alias already exists -> rollback + 409
Status change not possible -> rollback + 409
Invalid request -> rollback + 400
URL not found -> rollback + 404
DB unavailable -> rollback + 503
Bug/null pointer -> rollback + 500


Spring Security:
appuser handles user registration, authentication, and authorization.

JwtService:
- Generates signed JWT access tokens after successful login.
- Extracts userId and role from a token.
- Validates token signature and expiry.

JwtAuthenticationFilter:
- Runs once for each request.
- Reads Authorization: Bearer <JWT>.
- Validates the JWT.
- Places userId and role in Spring SecurityContext.

SecurityConfig:
- Defines public and protected endpoints.
- Uses stateless session management.
- Configures CORS for the React application.
- Registers JwtAuthenticationFilter before UsernamePasswordAuthenticationFilter.

Authorization flow

1. User logs in with email/userId and password.
2. UserAuthService verifies the BCrypt password hash.
3. JwtService generates a token with:
    - subject = userId
    - claim = role
    - expiration time
4. React sends the token in the Authorization header.
5. JwtAuthenticationFilter validates the token and sets Authentication.
6. UrlController gets authenticatedUserId from Authentication.getName().
7. UrlService checks:
    - ADMIN can access every URL.
    - USER can access only URL records where url.userId equals authenticatedUserId.
8. Unauthorized ownership attempts return 403 Forbidden.



Public
POST /v2/user/register
POST /v2/user/login/**
GET  /{shortCode}                 → redirect

Authenticated USER
POST /v2/url                      → creates only own URL
GET  /v2/url/me                   → own URLs
GET/PUT /v2/url/{id}              → only own URL
PUT activate/deactivate/{id}      → only own URL

Authenticated ADMIN
Everything above
GET /v2/url                       → every URL
GET /v2/url/custom/{customCode}   → lookup any alias
Can modify any URL



For this project, i'll use JWT-based authentication.

Flow:
POST /v2/user/register
POST /v2/user/login -> returns JWT token

Client calls:
Authorization: Bearer <token>

Backend validates token on every protected request

For now, logout can be simple:
Client deletes token

Later, advanced logout:
Store blacklisted tokens in Redis until expiry

User API endpoints (Controller):
POST /v2/user/register
POST /v2/user/login
POST /v2/user/logout
GET  /v2/user/me



Entity : 

1.Url Models

TABLE : url (unique constraint = {"short_code", "status"}, {"short_code"})
-----------------------------------------------------------------------------
id ----------- PK
user_id
original_url
short_code --- FK
status (A / D)
expiry_date
click_count
created_at
updated_at


TABLE : custom_url (unique constraint = {"short_code", "custom_code"})
-----------------------------------------------------------------------------
id ----------- PK
custom_code
short_code --- FK
created_at
updated_at



2. User Model

TABLE : users
------------------------------------------------
id ----------- PK
user_id
password_hash
name
email
role
status
created_at
updated_at




