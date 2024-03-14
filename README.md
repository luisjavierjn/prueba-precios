# PRUEBA PRECIOS

This project use Flyway to handle modifications to the database H2, the project has been Dockerized, and  
it runs with a "docker-compose up" command, coverage is above 90%, it uses ParameterizedTest to perform all  
the tests suggested by the exercise document.  

Three endpoints were built:  
* To get all the prices from the database H2 ("/api/v1/all")  
* To get the applicable ranges for a specific target date ("/api/v1/ranges")  
* To get the output data requested by the exercise ("/api/v1")  

When requesting the output data for a target date some logic is executed to determine the exact period of  
time the price for such date will be available, it may happen that there is a period of time ahead with a  
higher priority, so the end of the current price will be the beginning of the next period with a higher  
priority price.  

## docker-compose up

![Forcing Dockerfile](./images/Screenshot%20from%202024-03-14%2002-01-57.png)  
![Spin up instance](./images/Screenshot%20from%202024-03-14%2002-02-08.png)  
![Spin up completed](./images/Screenshot%20from%202024-03-14%2002-02-14.png)  

## H2

![H2 Login Console](./images/Screenshot%20from%202024-03-14%2001-55-23.png)  
![H2 Database](./images/Screenshot%20from%202024-03-14%2001-56-24.png)  

## getting all the prices from the database H2 ("/api/v1/all")

![all](./images/Screenshot%20from%202024-03-14%2001-57-33.png)  

## getting the applicable ranges for a specific target date ("/api/v1/ranges")

![ranges](./images/Screenshot%20from%202024-03-14%2001-57-54.png)  

## getting the output data requested by the exercise ("/api/v1")  

### Test 1: request at 10:00 a.m. on the 14th for product 35455 for brand 1 (XYZ)  

![test1](./images/Screenshot%20from%202024-03-14%2001-58-45.png)  

### Test 2: request at 4:00 p.m. on the 14th for product 35455 for brand 1 (XYZ)  

![test2](./images/Screenshot%20from%202024-03-14%2001-59-13.png)  

### Test 3: request at 9:00 p.m. on day 14th for product 35455 for brand 1 (XYZ)  

![test3](./images/Screenshot%20from%202024-03-14%2001-59-53.png)  

### Test 4: request at 10:00 a.m. on the 15th for product 35455 for brand 1 (XYZ)  

![test4](./images/Screenshot%20from%202024-03-14%2002-00-28.png)  

### Test 5: request at 9:00 p.m. on day 16th for product 35455 for brand 1 (XYZ)  

![test5](./images/Screenshot%20from%202024-03-14%2002-01-02.png)  



