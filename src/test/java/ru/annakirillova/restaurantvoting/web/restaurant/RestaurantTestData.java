package ru.annakirillova.restaurantvoting.web.restaurant;

import ru.annakirillova.restaurantvoting.model.Restaurant;
import ru.annakirillova.restaurantvoting.to.RestaurantTo;
import ru.annakirillova.restaurantvoting.web.MatcherFactory;

import java.util.List;

public class RestaurantTestData {

    public static final int RESTAURANT1_ID = 1;

    public static final MatcherFactory.Matcher<Restaurant> RESTAURANT_MATCHER = MatcherFactory.usingIgnoringFieldsComparator(Restaurant.class, "meals", "votes");

    public static final Restaurant dickinson = new Restaurant(RESTAURANT1_ID, "DICKINSON");
    public static final Restaurant voltaire = new Restaurant(RESTAURANT1_ID + 1, "VOLTAIRE");
    public static final Restaurant dante = new Restaurant(RESTAURANT1_ID + 2, "DANTE");
    public static final Restaurant chekhov = new Restaurant(RESTAURANT1_ID + 3, "CHEKHOV");
    public static final Restaurant hemingway = new Restaurant(RESTAURANT1_ID + 4, "HEMINGWAY");

    public static final List<Restaurant> restaurants = List.of(dickinson, voltaire, dante, chekhov, hemingway);

    public static MatcherFactory.Matcher<RestaurantTo> RESTAURANT_TO_MATCHER = MatcherFactory.usingEqualsComparator(RestaurantTo.class);


    public static Restaurant getNew() {
        return new Restaurant(null, "New Restaurant");
    }

    public static Restaurant getUpdated() {
        return new Restaurant(RESTAURANT1_ID, "Updated Restaurant");
    }
}
