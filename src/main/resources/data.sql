INSERT INTO users (name, email, password)
VALUES ('User1', 'user1@yandex.ru', '{noop}password1'),
       ('User2', 'user2@yandex.ru', '{noop}password2'),
       ('User3', 'user3@yandex.ru', '{noop}password3'),
       ('Admin', 'admin@gmail.com', '{noop}admin');

INSERT INTO user_role (role, user_id)
VALUES ('USER', 1),
       ('USER', 2),
       ('USER', 3),
       ('ADMIN', 4);

INSERT INTO restaurant (name)
VALUES ('DICKINSON'),
       ('VOLTAIRE'),
       ('DANTE'),
       ('CHEKHOV'),
       ('HEMINGWAY');

INSERT INTO meal (restaurant_id, description, price)
VALUES (1, 'Antipasto Salad', 500),
       (1, 'Chicken Grilled', 600),
       (1, 'Pineapple Cake', 300),
       (2, 'BBQ Pork Salad', 500),
       (2, 'Beef Burger', 600),
       (2, 'Chicken Wrap', 500),
       (3, 'Caesar Salad', 500),
       (3, 'Beef Meal', 600),
       (4, 'Chicken Salad', 500),
       (4, 'Rice Bowl', 600),
       (4, 'Coffee cake', 300),
       (5, 'Crispy Salad', 500),
       (5, 'Chicken Marsala', 600),
       (5, 'Mango Meringue Cake', 300);

INSERT INTO vote (user_id, restaurant_id)
VALUES (1, 1),
       (2, 1),
       (3, 2);
