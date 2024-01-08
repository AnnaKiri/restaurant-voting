
Swagger documentation http://localhost:8080/swagger-ui/index.html


# Restaurant voting system

Enterprise Java project with registration/authorization and access rights based on roles (USER, ADMINISTRATOR).
The administrator can create / view / edit / delete - dishes / menus / restaurants / users.
Users can manage their profile, view restaurants and their menus, and vote via the REST interface with basic authorization.
The entire REST interface is covered by JUnit tests using Spring MVC Test and Spring Security Test.

### Description:
<pre>
  Build a voting system for deciding where to have lunch.

* 2 types of users: admin and regular users
* Admin can input a restaurant and it's lunch menu of the day (2-5 items usually, just a dish name and price)
* Menu changes each day (admins do the updates)
* Users can vote for a restaurant they want to have lunch at today
* Only one vote counted per user
* If user votes again the same day:
 - If it is before 11:00 we assume that he changed his mind.
 - If it is after 11:00 then it is too late, vote can't be changed
 </pre> 
***
### Technology stack:
**Maven** / **Hibernate** / **Spring Boot 3.2.1** / **Spring Data JPA** / **Spring Security** / **Spring MVC** / **HSQLDB** / **JUnit** / **EHCACHE** / **Apache Tomcat** / **Json Jackson** / **SLF4J**

***
### Swagger REST Api Documentation:
- http://localhost:8080/swagger-ui/index.html
- http://localhost:8080/v3/api-docs
***

### `Admin`

### AdminMealController      `/admin/restaurants/2/meals`
- Create a Meal for the Restaurant

`curl -X POST http://localhost:8080/admin/restaurants/2/meals 
-H 'Content-Type: application/json' 
-d '{"name": "New Meal", "price": "146"}' 
--user admin@gmail.com:admin`

- Create a List of Meals for a Restaurant

`curl -X POST http://localhost:8080/admin/restaurants/2/meals/add-list 
-H 'Content-Type: application/json' 
-d '[{"name": "New Meal1", "price": "155"}, {"name": "New Meal2", "price": "188"}, {"name": "New Meal3", "price": "146"}]' 
--user admin@gmail.com:admin`

- Delete a Meal

`curl -X DELETE http://localhost:8080/admin/restaurants/2/meals/1 --user admin@gmail.com:admin`

- Update a Meal

`curl -X PUT http://localhost:8080/admin/restaurants/2/meals/1 
-H 'Content-Type: application/json' 
-d '{"name": "Updated Meal", "price": "455"}' 
--user admin@gmail.com:admin`

- Get All Meals for a Restaurant

`curl -X GET http://localhost:8080/admin/restaurants/2/meals --user admin@gmail.com:admin`

- Get Meals Between Dates for a Restaurant

`curl -X GET http://localhost:8080/admin/restaurants/2/meals/filter?startDate=2024-01-02&endDate=2024-01-03 --user admin@gmail.com:admin`

- Get a Specific Meal of a Restaurant

`curl -X GET http://localhost:8080/admin/restaurants/2/meals/1 --user admin@gmail.com:admin`

- Get All Meals for Today for a Restaurant

`curl -X GET http://localhost:8080/admin/restaurants/2/meals/today --user admin@gmail.com:admin`

### AdminRestaurantController      `/admin/restaurants`

- Create a Restaurant

`curl -X POST http://localhost:8080/admin/restaurants 
-H 'Content-Type: application/json' 
-d '{"name": "NewRestaurant"}' 
--user admin@gmail.com:admin`

- Delete a Restaurant

`curl -X DELETE http://localhost:8080/admin/restaurants/2 --user admin@gmail.com:admin`

- Update a Restaurant

`curl -X PUT http://localhost:8080/admin/restaurants/2 
-H 'Content-Type: application/json' 
-d '{"name": "Updated Restaurant"}' 
--user admin@gmail.com:admin`

- Get All Restaurants With Meals

`curl -X GET 'http://localhost:8080/admin/restaurants?meals=true --user admin@gmail.com:admin`

- Get All Restaurants 

`curl -X GET 'http://localhost:8080/admin/restaurants --user admin@gmail.com:admin`

- Get a Specific Restaurant With Meals

`curl -X GET 'http://localhost:8080/admin/restaurants/2?meals=true --user admin@gmail.com:admin`

- Get a Specific Restaurant

`curl -X GET 'http://localhost:8080/admin/restaurants/2 --user admin@gmail.com:admin`

### AdminVoteController      `/admin/restaurants/2/votes`

- Delete a Vote

`curl -X DELETE http://localhost:8080/admin/restaurants/2/votes/1 --user admin@gmail.com:admin`

- Get All Votes for a Restaurant

`curl -X GET http://localhost:8080/admin/restaurants/2/votes --user admin@gmail.com:admin`

- Get Votes Between Dates for a Restaurant

`curl -X GET http://localhost:8080/admin/restaurants/2/votes/filter?startDate=2024-01-01&endDate=2024-01-04 --user admin@gmail.com:admin`

- Get All Votes for Today for a Restaurant

`curl -X GET http://localhost:8080/admin/restaurants/2/votes/today --user admin@gmail.com:admin`

- Get a Specific Vote for a Restaurant

`curl -X GET http://localhost:8080/admin/restaurants/2/votes/1 --user admin@gmail.com:admin`

### AdminUserController      `/admin/users`

- Get a Specific user

`curl -X GET http://localhost:8080/admin/users/1 --user admin@gmail.com:admin`

- Update a user

`curl -X PUT http://localhost:8080/admin/users/1 
-H "Content-Type: application/json" 
-d '{ "name": "UpdatedUser1", "email": "updatedemail@yandex.ru", "password": "updatedpassword", "roles": ["USER"] }'
--user admin@gmail.com:admin`

- Delete a user

`curl -X DELETE http://localhost:8080/admin/users/1 --user admin@gmail.com:admin`

- Enable or disable a user

`curl -X PATCH http://localhost:8080/admin/users/1?enabled=false --user admin@gmail.com:admin`

- Get All users

`curl -X GET http://localhost:8080/admin/users --user admin@gmail.com:admin`

- Create New user

`curl -X POST http://localhost:8080/admin/users
-H "Content-Type: application/json" 
-d '{ "name": "NewUser", "email": "newemail@yandex.ru", "password": "newpassword" }'
--user admin@gmail.com:admin`

- Get user by email

`curl -X GET http://localhost:8080/admin/users/by-email?email=user1@yandex.ru --user admin@gmail.com:admin`

### `User`

### UserRestaurantController      `/user/restaurants`

- Get a Restaurant by id with meals and rating

`curl -X GET http://localhost:8080/user/restaurants/1/with-meals-and-rating --user user1@yandex.ru:password1`

- Get All Restaurants with meals and rating

`curl -X GET http://localhost:8080/user/restaurants?meals=true&votes=true --user user1@yandex.ru:password1`

- Get All Restaurants with meals

`curl -X POST http://localhost:8080/user/restaurants?meals=true --user user1@yandex.ru:password1`

- Get All Restaurants with rating

`curl -X POST http://localhost:8080/user/restaurants?votes=true --user user1@yandex.ru:password1`

- Get All Restaurants

`curl -X POST http://localhost:8080/user/restaurants --user user1@yandex.ru:password1`

### UserVoteController      `/user/restaurants/{restaurantId}/votes`

- Create a vote

`curl -X POST http://localhost:8080/user/restaurants/4/votes --user user1@yandex.ru:password1`

### ProfileController      `/profile`

- Create New User

`curl -X POST http://localhost:8080/profile 
-H "Content-Type: application/json" 
-d '{ "name": "NewUser", "email": "newuseremail@yandex.ru", "password": "password4" }'`

- Get the currently logged-in user

`curl -X GET http://localhost:8080/profile --user user1@yandex.ru:password1`

- Update the currently logged-in user

`curl -X PUT http://localhost:8080/profile 
-H "Content-Type: application/json" 
-d '{ "name": "UpdatedUser1", "email": "updateduser1@yandex.ru", "password": "updatedpassword1" }' 
--user user1@yandex.ru:password1`

- Delete the currently logged-in user

`curl -X DELETE http://localhost:8080/profile --user user1@yandex.ru:password1`
