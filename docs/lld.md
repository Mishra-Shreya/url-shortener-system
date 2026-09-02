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
|   |
|   |-- dto
|   |   |



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


URL API endpoints (Controller):
GET  /v2/url
GET  /v2/url/{id}
GET  /v2/url/custom/{customCode}
POST /v2/url
PUT  /v2/url/deactivate/{id}
PUT  /v2/url/activate/{id}
PUT  /v2/url/{id}

Custom alias already exists -> rollback + 409
Status change not possible -> rollback + 409
Invalid request -> rollback + 400
URL not found -> rollback + 404
DB unavailable -> rollback + 503
Bug/null pointer -> rollback + 500



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




