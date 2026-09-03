Initial aim is:
User can register -> login -> create short URL -> get redirected

Notes:

ResponseEntity - used to return custom http resp status codes

RequestParam    vs  PathVariable
/url/{id}       vs  /url?id=21

NamedQueries    vs  NativeQueries
we write jpql   vs  we write actual sql statement
used in entiy   vs  used in repository / service

@Query - used for custom query in repository class. we can write custom queries in jpql or native query

@Param - used to map method arg with jpql arg / native query arg if they are different
eg. if named query:
name = "Url.findByUserId"
query = "SELECT e FROM Url e WHERE e.usedId = :user"
 
inside repository:
public findByUserId(@Param('user') String user);


SPRING VALIDATIONS
@Valid in controller

add below in dto fields
@NotBlank(message = "User ID is required")




Spring Security:

identity + login + password hashing + tokens + filters + authorization rules

First: Concepts

Authentication means: Who are you?
Example:
email + password -> verify user exists -> password matches -> login successful

Authorization means:
What are you allowed to do?
Example:
    User A can update User A's URLs
    User A cannot update User B's URLs
    Admin can see all URLs

Login means user proves identity.
Logout depends on auth style:
    session-based logout: server destroys session
    JWT logout: client deletes token, or server blacklists token if you want stricter logout


Spring Security is the framework that intercepts requests before they reach your controller.
Flow:
    HTTP request
        |
    Spring Security Filter Chain
        |
    Check token/session
        |
    Set authenticated user in SecurityContext
        |
    Controller runs

What Auth Style Should You Use?
For this project, using JWT-based authentication.
Bcz?
    Common in modern REST APIs
    Works well with frontend later
    Stateless backend
    Good for interviews

