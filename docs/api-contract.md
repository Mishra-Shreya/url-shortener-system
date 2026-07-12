
I] URL endpoints:

1. GET /v2/url

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


---------------------------------------------------------------------


2. GET /v2/url/{id}

request: blank
response:
{
    "success": true,
    "respCode": "SUCCESS",
    "respDescription": "Success",
    "data": {
        "id": 2026192001000000031,
        "originalUrl": "https://leetcode.com/",
        "shortCode": "Qk1obBqy1gB",
        "customCode": "leet3",
        "shortUrl": "http://localhost:8080/leet3",
        "expiryDate": "2027-07-11T19:19:54.385154",
        "status": "D"
    },
    "timeStamp": "2026-07-11T20:37:28.739461"
}
response: {id not found}
{
    "success": false,
    "respCode": "ID_NOT_FOUND",
    "respDescription": "ID not found",
    "data": null,
    "timeStamp": "2026-07-12T10:42:54.399941037"
}
response: {invalid id}
{
    "success": false,
    "respCode": "INVALID_ID",
    "respDescription": "Invalid ID",
    "data": null,
    "timeStamp": "2026-07-12T10:43:45.222832621"
}


---------------------------------------------------------------------


3. GET /v2/url/custom/{customCode}

request: blank
response:
{
    "success": true,
    "respCode": "RECORDS_FETCHED",
    "respDescription": "Records fetched successfully",
    "data": [
        {
            "id": 2026192001000000021,
            "originalUrl": "https://leetcode.com/",
            "shortCode": "Gk1obBqy1gB",
            "customCode": "leet2",
            "shortUrl": "http://localhost:8080/leet2",
            "expiryDate": "2027-07-11T15:23:09.225214",
            "status": "D"
        },
        {
            "id": 2026192001000000024,
            "originalUrl": "https://leetcode.com/abc",
            "shortCode": "Jk1obBqy1gB",
            "customCode": "leet2",
            "shortUrl": "http://localhost:8080/leet2",
            "expiryDate": "2027-07-11T17:00:41.5845",
            "status": "A"
        }
    ],
    "timeStamp": "2026-07-11T19:38:47.5763143"
}
response: {shortcode not found}
{
    "success": false,
    "respCode": "SHORTCODE_NOT_FOUND",
    "respDescription": "Shortcode not found",
    "data": null,
    "timeStamp": "2026-07-12T10:44:25.592772121"
}


---------------------------------------------------------------------


4. POST /v2/url

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


---------------------------------------------------------------------


5. PUT /v2/url/deactivate/{id}

request: blank
response:
{
    "success": true,
    "respCode": "SHORTURL_DEACTIVATED",
    "respDescription": "Short URL deactivated successfully",
    "data": {
        "id": 2026192001000000001,
        "originalUrl": "https://leetcode.com/",
        "shortCode": "zj1obBqy1gB",
        "customCode": "leet",
        "shortUrl": "https://url-shortener-system-csrr.onrender.com/leet",
        "expiryDate": "2027-07-11T21:16:28.234666",
        "status": "D"
    },
    "timeStamp": "2026-07-12T10:46:44.394702728"
}
response: {shortcode already deactivated}
{
    "success": true,
    "respCode": "SHORTURL_ALREADY_DEACTIVATED",
    "respDescription": "Short URL already deactivated",
    "data": {
        "id": 2026192001000000001,
        "originalUrl": "https://leetcode.com/",
        "shortCode": "zj1obBqy1gB",
        "customCode": "leet",
        "shortUrl": "https://url-shortener-system-csrr.onrender.com/leet",
        "expiryDate": "2027-07-11T21:16:28.234666",
        "status": "D"
    },
    "timeStamp": "2026-07-11T21:19:38.716576057"
}
response: {invalid id}
{
    "success": false,
    "respCode": "INVALID_ID",
    "respDescription": "Invalid ID",
    "data": null,
    "timeStamp": "2026-07-12T10:47:22.498325866"
}


---------------------------------------------------------------------


6. PUT /v2/url/activate/{id}

request: blank
response:
{
    "success": true,
    "respCode": "SHORTURL_ACTIVATED",
    "respDescription": "Short URL activated successfully",
    "data": {
        "id": 2026192001000000001,
        "originalUrl": "https://leetcode.com/",
        "shortCode": "zj1obBqy1gB",
        "customCode": "leet",
        "shortUrl": "https://url-shortener-system-csrr.onrender.com/leet",
        "expiryDate": "2027-07-11T21:16:28.234666",
        "status": "A"
    },
    "timeStamp": "2026-07-11T21:19:47.615853974"
}
response: {url already active}
{
    "success": true,
    "respCode": "SHORTURL_ALREADY_ACTIVE",
    "respDescription": " Short URL already active",
    "data": {
        "id": 2026192001000000001,
        "originalUrl": "https://leetcode.com/",
        "shortCode": "zj1obBqy1gB",
        "customCode": "leet",
        "shortUrl": "https://url-shortener-system-csrr.onrender.com/leet",
        "expiryDate": "2027-07-11T21:16:28.234666",
        "status": "A"
    },
    "timeStamp": "2026-07-12T10:47:59.492053002"
}
response:
{
    "success": false,
    "respCode": "INVALID_ID",
    "respDescription": "Invalid ID",
    "data": null,
    "timeStamp": "2026-07-12T10:48:37.111457113"
}

---------------------------------------------------------------------


7. PUT /v2/url/{id}

request:
{
    "originalUrl" : "https://leetcode.com/XYZzz",
    "customCode" : "leeto",
    "status" : "A"
}
response:
{
    "success": true,
    "respCode": "URL_UPDATED",
    "respDescription": "URL updated successfully",
    "data": {
        "id": 2026192001000000001,
        "originalUrl": "https://leetcode.com/ABC",
        "shortCode": "zj1obBqy1gB",
        "customCode": "leeto",
        "shortUrl": "https://url-shortener-system-csrr.onrender.com/leeto",
        "expiryDate": "2027-07-11T21:16:28.234666",
        "status": "A"
    },
    "timeStamp": "2026-07-12T10:50:13.0932606"
}
response:
{
    "success": false,
    "respCode": "INVALID_ID",
    "respDescription": "Invalid ID",
    "data": null,
    "timeStamp": "2026-07-12T10:51:40.08412657"
}
response:
{
    "success": false,
    "respCode": "CUSTOM_ALIAS_ALREADY_EXISTS",
    "respDescription": "Custom alias already exists",
    "data": null,
    "timeStamp": "2026-07-12T10:51:52.361879305"
}
//similarly other validations 


