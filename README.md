# integrated-project-enviroment

11:24 - 24/4/2024

## Ajarn olarn
- set timeZone in database
- use type ZonedDateTime + toString()  in backend , for send timezone while API
- pull mysql/mysql-server on docker (docker run --rm mysql/mysql-server date , docker run --rm --env TZ=Asia/Bangkok mysql/mysql-server date)
- check on openjdk image (docker run --rm --env TZ=Asia/Bangkok openjdk date)
- https://developer.mozilla.org/en-US/docs/Web/JavaScript/Reference/Global_Objects/Date/toLocaleString
