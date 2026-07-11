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



