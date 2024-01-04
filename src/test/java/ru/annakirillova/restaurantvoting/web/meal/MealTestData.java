package ru.annakirillova.restaurantvoting.web.meal;

import ru.annakirillova.restaurantvoting.model.Meal;
import ru.annakirillova.restaurantvoting.web.MatcherFactory;

import java.time.LocalDate;
import java.time.Month;
import java.util.List;

import static ru.annakirillova.restaurantvoting.web.restaurant.RestaurantTestData.*;

public class MealTestData {
    public static final MatcherFactory.Matcher<Meal> MEAL_MATCHER = MatcherFactory.usingIgnoringFieldsComparator(Meal.class, "restaurant");
    public static final int MEAL1_ID = 1;

    public static final Meal meal1 = new Meal(MEAL1_ID, LocalDate.of(2024, Month.JANUARY, 10), "Antipasto Salad", 500, dickinson);
    public static final Meal meal2 = new Meal(MEAL1_ID + 1, LocalDate.of(2024, Month.JANUARY, 10), "Chicken Grilled", 600, dickinson);
    public static final Meal meal3 = new Meal(MEAL1_ID + 2, LocalDate.of(2024, Month.JANUARY, 10), "Pineapple Cake", 300, dickinson);

    public static final Meal meal4 = new Meal(MEAL1_ID + 3, LocalDate.of(2024, Month.JANUARY, 10), "BBQ Pork Salad", 500, voltaire);
    public static final Meal meal5 = new Meal(MEAL1_ID + 4, LocalDate.of(2024, Month.JANUARY, 10), "Beef Burger", 600, voltaire);
    public static final Meal meal6 = new Meal(MEAL1_ID + 5, LocalDate.of(2024, Month.JANUARY, 10), "Chicken Wrap", 500, voltaire);

    public static final Meal meal7 = new Meal(MEAL1_ID + 6, LocalDate.of(2024, Month.JANUARY, 10), "Caesar Salad", 500, dante);
    public static final Meal meal8 = new Meal(MEAL1_ID + 7, LocalDate.of(2024, Month.JANUARY, 10), "Beef Meal", 600, dante);

    public static final Meal meal9 = new Meal(MEAL1_ID + 8, LocalDate.of(2024, Month.JANUARY, 10), "Chicken Salad", 500, chekhov);
    public static final Meal meal10 = new Meal(MEAL1_ID + 9, LocalDate.of(2024, Month.JANUARY, 10), "Rice Bowl", 600, chekhov);
    public static final Meal meal11 = new Meal(MEAL1_ID + 10, LocalDate.of(2024, Month.JANUARY, 10), "Coffee cak", 300, chekhov);

    public static final Meal meal12 = new Meal(MEAL1_ID + 11, LocalDate.of(2024, Month.JANUARY, 10), "Crispy Salad", 500, hemingway);
    public static final Meal meal13 = new Meal(MEAL1_ID + 12, LocalDate.of(2024, Month.JANUARY, 10), "Chicken Marsala", 600, hemingway);
    public static final Meal meal14 = new Meal(MEAL1_ID + 13, LocalDate.of(2024, Month.JANUARY, 10), "Mango Meringue Cake", 300, hemingway);

    public static final List<Meal> meals = List.of(meal14, meal13, meal12, meal11, meal10, meal9, meal8, meal7, meal6, meal5, meal4, meal3, meal2, meal1);

    public static Meal getNew() {
        return new Meal(null, LocalDate.of(2024, Month.JANUARY, 10), "New Meal", 146, dickinson);
    }

    public static Meal getUpdated() {
        return new Meal(MEAL1_ID, meal1.getDate(), "Updated Meal", 455, dickinson);
    }
}
