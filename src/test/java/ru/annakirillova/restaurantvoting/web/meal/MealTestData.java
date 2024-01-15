package ru.annakirillova.restaurantvoting.web.meal;

import ru.annakirillova.restaurantvoting.model.Meal;
import ru.annakirillova.restaurantvoting.web.MatcherFactory;

import java.time.LocalDate;
import java.util.List;

public class MealTestData {
    public static final MatcherFactory.Matcher<Meal> MEAL_MATCHER = MatcherFactory.usingIgnoringFieldsComparator(Meal.class, "restaurant");
    public static final int MEAL1_ID = 1;
    public static final int NOT_FOUND = 100;

    public static final Meal meal1 = new Meal(MEAL1_ID, LocalDate.now(), "Antipasto Salad", 500);
    public static final Meal meal2 = new Meal(MEAL1_ID + 1, LocalDate.now(), "Chicken Grilled", 600);
    public static final Meal meal3 = new Meal(MEAL1_ID + 2, LocalDate.now(), "Pineapple Cake", 300);
    public static final Meal newYearMeal1 = new Meal(MEAL1_ID + 14, LocalDate.of(2024, 1, 1), "Olivye salad", 89);

    public static final List<Meal> mealsForDickinsonToday = List.of(meal1, meal2, meal3);
    public static final List<Meal> mealsForDickinson = List.of(meal1, meal2, meal3, newYearMeal1);

    public static final Meal meal4 = new Meal(MEAL1_ID + 3, LocalDate.now(), "BBQ Pork Salad", 500);
    public static final Meal meal5 = new Meal(MEAL1_ID + 4, LocalDate.now(), "Beef Burger", 600);
    public static final Meal meal6 = new Meal(MEAL1_ID + 5, LocalDate.now(), "Chicken Wrap", 500);
    public static final Meal newYearMeal2 = new Meal(MEAL1_ID + 15, LocalDate.of(2024, 1, 1), "Shuba salad", 99);

    public static final Meal meal7 = new Meal(MEAL1_ID + 6, LocalDate.now(), "Caesar Salad", 500);
    public static final Meal meal8 = new Meal(MEAL1_ID + 7, LocalDate.now(), "Beef Meal", 600);
    public static final Meal newYearMeal3 = new Meal(MEAL1_ID + 16, LocalDate.of(2024, 1, 1), "Zalivnaya ryba", 109);

    public static final Meal meal9 = new Meal(MEAL1_ID + 8, LocalDate.now(), "Chicken Salad", 500);
    public static final Meal meal10 = new Meal(MEAL1_ID + 9, LocalDate.now(), "Rice Bowl", 600);
    public static final Meal meal11 = new Meal(MEAL1_ID + 10, LocalDate.now(), "Coffee cake", 300);
    public static final Meal newYearMeal4 = new Meal(MEAL1_ID + 17, LocalDate.of(2024, 1, 1), "Pelmeni", 119);

    public static final Meal meal12 = new Meal(MEAL1_ID + 11, LocalDate.now(), "Crispy Salad", 500);
    public static final Meal meal13 = new Meal(MEAL1_ID + 12, LocalDate.now(), "Chicken Marsala", 600);
    public static final Meal meal14 = new Meal(MEAL1_ID + 13, LocalDate.now(), "Mango Meringue Cake", 300);
    public static final Meal newYearMeal5 = new Meal(MEAL1_ID + 18, LocalDate.of(2024, 1, 1), "Holodec", 129);

    public static final Meal newMeal1 = new Meal(MEAL1_ID + 19, LocalDate.now(), "New Meal1", 155);
    public static final Meal newMeal2 = new Meal(MEAL1_ID + 20, LocalDate.now(), "New Meal2", 188);
    public static final Meal newMeal3 = new Meal(MEAL1_ID + 21, LocalDate.now(), "New Meal3", 146);

    public static final List<Meal> newMeals = List.of(newMeal1, newMeal2, newMeal3);

    public static Meal getNew() {
        return new Meal(null, LocalDate.now(), "New Meal", 146);
    }

    public static Meal getUpdated() {
        return new Meal(MEAL1_ID, meal1.getAvailableOn(), "Updated Meal", 455);
    }
}
