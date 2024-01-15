package ru.annakirillova.restaurantvoting.web.dish;

import ru.annakirillova.restaurantvoting.model.Dish;
import ru.annakirillova.restaurantvoting.web.MatcherFactory;

import java.time.LocalDate;
import java.util.List;

public class DishTestData {
    public static final MatcherFactory.Matcher<Dish> DISH_MATCHER = MatcherFactory.usingIgnoringFieldsComparator(Dish.class, "restaurant");
    public static final int DISH1_ID = 1;
    public static final int NOT_FOUND = 100;

    public static final Dish DISH_1 = new Dish(DISH1_ID, LocalDate.now(), "Antipasto Salad", 500);
    public static final Dish DISH_2 = new Dish(DISH1_ID + 1, LocalDate.now(), "Chicken Grilled", 600);
    public static final Dish DISH_3 = new Dish(DISH1_ID + 2, LocalDate.now(), "Pineapple Cake", 300);
    public static final Dish NEW_YEAR_DISH_1 = new Dish(DISH1_ID + 14, LocalDate.of(2024, 1, 1), "Olivye salad", 89);

    public static final List<Dish> DISHES_FOR_DICKINSON_TODAY = List.of(DISH_1, DISH_2, DISH_3);
    public static final List<Dish> DISHES_FOR_DICKINSON = List.of(DISH_1, DISH_2, DISH_3, NEW_YEAR_DISH_1);

    public static final Dish DISH_4 = new Dish(DISH1_ID + 3, LocalDate.now(), "BBQ Pork Salad", 500);
    public static final Dish DISH_5 = new Dish(DISH1_ID + 4, LocalDate.now(), "Beef Burger", 600);
    public static final Dish DISH_6 = new Dish(DISH1_ID + 5, LocalDate.now(), "Chicken Wrap", 500);
    public static final Dish NEW_YEAR_DISH_2 = new Dish(DISH1_ID + 15, LocalDate.of(2024, 1, 1), "Shuba salad", 99);

    public static final Dish DISH_7 = new Dish(DISH1_ID + 6, LocalDate.now(), "Caesar Salad", 500);
    public static final Dish DISH_8 = new Dish(DISH1_ID + 7, LocalDate.now(), "Beef Meal", 600);
    public static final Dish NEW_YEAR_DISH_3 = new Dish(DISH1_ID + 16, LocalDate.of(2024, 1, 1), "Zalivnaya ryba", 109);

    public static final Dish DISH_9 = new Dish(DISH1_ID + 8, LocalDate.now(), "Chicken Salad", 500);
    public static final Dish DISH_10 = new Dish(DISH1_ID + 9, LocalDate.now(), "Rice Bowl", 600);
    public static final Dish DISH_11 = new Dish(DISH1_ID + 10, LocalDate.now(), "Coffee cake", 300);
    public static final Dish NEW_YEAR_DISH_4 = new Dish(DISH1_ID + 17, LocalDate.of(2024, 1, 1), "Pelmeni", 119);

    public static final Dish DISH_12 = new Dish(DISH1_ID + 11, LocalDate.now(), "Crispy Salad", 500);
    public static final Dish DISH_13 = new Dish(DISH1_ID + 12, LocalDate.now(), "Chicken Marsala", 600);
    public static final Dish DISH_14 = new Dish(DISH1_ID + 13, LocalDate.now(), "Mango Meringue Cake", 300);
    public static final Dish NEW_YEAR_DISH_5 = new Dish(DISH1_ID + 18, LocalDate.of(2024, 1, 1), "Holodec", 129);

    public static final Dish NEW_DISH_1 = new Dish(DISH1_ID + 19, LocalDate.now(), "New Dish1", 155);
    public static final Dish NEW_DISH_2 = new Dish(DISH1_ID + 20, LocalDate.now(), "New Dish2", 188);
    public static final Dish NEW_DISH_3 = new Dish(DISH1_ID + 21, LocalDate.now(), "New Dish3", 146);

    public static final List<Dish> NEW_DISHES = List.of(NEW_DISH_1, NEW_DISH_2, NEW_DISH_3);

    public static Dish getNew() {
        return new Dish(null, LocalDate.now(), "New Dish", 146);
    }

    public static Dish getUpdated() {
        return new Dish(DISH1_ID, DISH_1.getAvailableOn(), "Updated Dish", 455);
    }
}
