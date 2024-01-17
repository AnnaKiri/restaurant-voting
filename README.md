[![Codacy Badge](https://app.codacy.com/project/badge/Grade/80ad628e283145068a3d2d8e6ff8142a)](https://app.codacy.com/gh/AnnaKiri/restaurant-voting/dashboard?utm_source=gh&utm_medium=referral&utm_content=&utm_campaign=Badge_grade)
***

# Restaurant voting application

Enterprise Java project with registration/authorization and access rights based on roles (USER, ADMINISTRATOR).
The administrator can create / view / edit / delete - dishes / menus / restaurants / users.
Users can manage their profile, view restaurants and their menus, and vote via the REST interface with basic
authorization.
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
 - If it is after 11:00 then it is too late, vote can't be changed</pre>
***

### Technology stack:

**Maven** / **Hibernate** / **Spring Boot 3.2.1** / **Spring Data JPA** / **Spring Security** / **Spring MVC** / **H2** / **JUnit** / **EHCACHE** / **Apache Tomcat** / **Json Jackson** / **SLF4J**

***

### Swagger REST Api Documentation:

- http://localhost:8080/swagger-ui/index.html
- http://localhost:8080/v3/api-docs

***

### `Admin`

### AdminDishController      `/admin/restaurants/2/dishes`

- Create a Dish for the Restaurant

`curl -X POST http://localhost:8080/admin/restaurants/2/dishes
-H 'Content-Type: application/json'
-d '{"name": "New Dish", "price": "146"}'
--user admin@gmail.com:admin`

- Delete a Dish

`curl -X DELETE http://localhost:8080/admin/restaurants/2/dishes/1 --user admin@gmail.com:admin`

- Update a Dish

`curl -X PUT http://localhost:8080/admin/restaurants/2/dishes/1
-H 'Content-Type: application/json'
-d '{"name": "Updated Dish", "price": "455"}'
--user admin@gmail.com:admin`

- Get All Dishes for a Restaurant

`curl -X GET http://localhost:8080/admin/restaurants/2/dishes --user admin@gmail.com:admin`

- Get Dishes Between Dates for a Restaurant

`curl -X GET http://localhost:8080/admin/restaurants/3/dishes/filter?startDate=2024-01-01&endDate=2024-01-03 --user admin@gmail.com:admin`

- Get a Specific Dish of a Restaurant

`curl -X GET http://localhost:8080/admin/restaurants/2/dishes/1 --user admin@gmail.com:admin`

- Get All Dishes Today for a Restaurant

`curl -X GET http://localhost:8080/admin/restaurants/2/dishes/today --user admin@gmail.com:admin`

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

- Get All Restaurants With Dishes

`curl -X GET 'http://localhost:8080/admin/restaurants?dishesToday=true --user admin@gmail.com:admin`

- Get All Restaurants

`curl -X GET 'http://localhost:8080/admin/restaurants --user admin@gmail.com:admin`

- Get a Specific Restaurant With Dishes

`curl -X GET 'http://localhost:8080/admin/restaurants/2?dishesToday=true --user admin@gmail.com:admin`

- Get a Specific Restaurant

`curl -X GET 'http://localhost:8080/admin/restaurants/2 --user admin@gmail.com:admin`

### AdminUserController      `/admin/users`

- Create New user

`curl -X POST http://localhost:8080/admin/users
-H "Content-Type: application/json"
-d '{ "name": "NewUser", "email": "newemail@yandex.ru", "password": "newpassword" }'
--user admin@gmail.com:admin`

- Delete a user

`curl -X DELETE http://localhost:8080/admin/users/1 --user admin@gmail.com:admin`

- Update a user

`curl -X PUT http://localhost:8080/admin/users/1
-H "Content-Type: application/json"
-d '{ "name": "UpdatedUser1", "email": "updatedemail@yandex.ru", "password": "updatedpassword", "roles": ["USER"] }'
--user admin@gmail.com:admin`

- Get a Specific user

`curl -X GET http://localhost:8080/admin/users/1 --user admin@gmail.com:admin`

- Get All users

`curl -X GET http://localhost:8080/admin/users --user admin@gmail.com:admin`

- Get user by email

`curl -X GET http://localhost:8080/admin/users/by-email?email=user1@yandex.ru --user admin@gmail.com:admin`

- Enable or disable a user

`curl -X PATCH http://localhost:8080/admin/users/1?enabled=false --user admin@gmail.com:admin`

### `User`

### UserRestaurantController      `/user/restaurants`

- Get a Restaurant by id with dishes and rating

`curl -X GET http://localhost:8080/user/restaurants/1/with-dishes-and-rating --user user1@yandex.ru:password1`

- Get All Restaurants with dishes and rating

`curl -X GET http://localhost:8080/user/restaurants?dishesToday=true&ratingToday=true --user user1@yandex.ru:password1`

- Get All Restaurants with dishes

`curl -X POST http://localhost:8080/user/restaurants?dishesToday=true --user user1@yandex.ru:password1`

- Get All Restaurants with rating

`curl -X POST http://localhost:8080/user/restaurants?ratingToday=true --user user1@yandex.ru:password1`

- Get All Restaurants

`curl -X POST http://localhost:8080/user/restaurants --user user1@yandex.ru:password1`

### ProfileVoteController      `/profile/votes`

- Create a vote

`curl -X POST http://localhost:8080/profile/votes
-H "Content-Type: application/json"
-d '{ "restaurantId": "1" }'
--user user3@yandex.ru:password3`

- Update a vote

`curl -X PUT http://localhost:8080/profile/votes/1
-H "Content-Type: application/json"
-d '{ "restaurantId": "2" }' 
--user user1@yandex.ru:password1`

- Get a Vote by id

`curl -X GET http://localhost:8080/profile/votes/1 --user user1@yandex.ru:password1`

- Get a Vote for today

`curl -X GET http://localhost:8080/profile/votes/today --user user1@yandex.ru:password1`

- Get all Votes by user

`curl -X GET http://localhost:8080/profile/votes/ --user user1@yandex.ru:password1`

### ProfileController      `/profile`

- Create New User

`curl -X POST http://localhost:8080/profile
-H "Content-Type: application/json"
-d '{ "name": "NewUser", "email": "newuseremail@yandex.ru", "password": "password4" }'`

- Update the currently logged-in user

`curl -X PUT http://localhost:8080/profile
-H "Content-Type: application/json"
-d '{ "name": "UpdatedUser1", "email": "updateduser1@yandex.ru", "password": "updatedpassword1" }'
--user user1@yandex.ru:password1`

- Get the currently logged-in user

`curl -X GET http://localhost:8080/profile --user user1@yandex.ru:password1`

- Delete the currently logged-in user

`curl -X DELETE http://localhost:8080/profile --user user1@yandex.ru:password1`
