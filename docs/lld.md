src/main/java/com/urlshortener/backend
|
|-- BackendApplication.java
|
|-- common
|   |-- exception
|   |   |-- GlobalExceptionHandler.java
|   |   |-- UrlShortenerException.java
|   |
|   |-- response
|   |   |-- ApiResponse.java
|   |   |-- ApiResponseBuilder.java
|   |   |-- ResponseCode.java
|   |   |-- ResponseType.java
|
|-- url
|   |-- controller
|   |   |-- UrlController.java
|   |
|   |-- service
|   |   |-- UrlService.java
|   |
|   |-- repository
|   |   |-- UrlRepository.java
|   |   |-- CustomUrlRepository.java
|   |
|   |-- entity
|   |   |-- Url.java
|   |   |-- CustomUrl.java
|   |
|   |-- dto
|   |   |-- request
|   |   |   |-- UrlRequestDto.java
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
|       |-- RedirectService.java
|
|-- shortcode
|   |-- service
|   |   |-- SequenceService.java
|   |
|   |-- ShortCodeGenerator.java
|   |-- ShortCodeGenerationStrategy.java
|   |-- Base62ShortCodeGenerator.java
|
|-- config
|   |-- AppProperties.java
|   |-- WebConfig.java



common contains reusable project-wide things:
common/exception
common/response

url contains URL management:
create URL
update URL
delete URL
activate/deactivate URL
fetch user URLs

redirect contains redirect flow:
GET /{shortCode}
resolve original URL
increase click count
redirect

shortcode contains ID/short code generation logic. 
This deserves its own package because it is an important design piece.


Sample request response:
GET/url
request: blank
response: (url/s found)
{
    "success": true,
    "responseCode": "SUCCESS",
    "respDescription": "Success",
    "data":
        {
            //all urls
        },
    "timestamp": "2026-07-09T04:30:00"
}
response: (url/s not found) 
{
    "success": false,
    "responseCode": "URL_NOT_FOUND",
    "respDescription": "URL not found",
    "data": null,
    "timestamp": "2026-07-09T04:30:00"
}

POST/url
request:
{
    "originalUrl": "",
    "shortCode": "", //optional
    "userId" : "Shreya"
}
response: (url shortened)
{
    "success": true,
    "respCode": "URL_CREATED",
    "respDescription": "Short URL created successfully",
    "data": {
        "id": 2026191001000000020,
        "originalUrl": "https://leetcode.com/",
        "shortCode": "eBwLlxcy1gB",
        "shortUrl": "http://localhost:8080/eBwLlxcy1gB",
        "expiryDate": "2027-07-10T02:13:43.2252869",
        "status": "A"
    },
    "timeStamp": "2026-07-10T02:13:43.2413139"
}
response: (handled failure) 
{
    "success": false,
    "responseCode": "CUSTOM_ALIAS_ALREADY_EXISTS",
    "respDescription": "Custom alias already exists",
    "data": null,
    "timestamp": "2026-07-09T04:30:00"
}
response: (url validation failed)
{
    "success": false,
    "responseCode": "VALIDATION_FAILED",
    "respDescription": "Request validation failed",
    "data": null,
    "timestamp": "2026-07-09T04:30:00"
}
response: (db voilation)
{
    "success": false,
    "responseCode": "DATA_INTEGRITY_VIOLATION",
    "respDescription": "Request conflicts with existing data",
    "data": null,
    "timestamp": "2026-07-09T04:30:00"
}
response: (general failure)
{
    "success": false,
    "responseCode": "INTERNAL_ERROR",
    "respDescription": "Something went wrong. Please try again later.",
    "data": null,
    "timestamp": "2026-07-09T04:30:00"
}



Custom alias already exists -> rollback + 409
Invalid request -> rollback + 400
URL not found -> rollback/no rollback + 404
DB unavailable -> rollback + 503
Bug/null pointer -> rollback + 500