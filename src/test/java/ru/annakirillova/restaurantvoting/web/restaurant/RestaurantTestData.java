package ru.annakirillova.restaurantvoting.web.restaurant;

import ru.annakirillova.restaurantvoting.model.Restaurant;
import ru.annakirillova.restaurantvoting.to.RestaurantTo;
import ru.annakirillova.restaurantvoting.web.MatcherFactory;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static ru.annakirillova.restaurantvoting.web.meal.MealTestData.*;
import static ru.annakirillova.restaurantvoting.web.vote.VoteTestData.votesForRestaurant1;
import static ru.annakirillova.restaurantvoting.web.vote.VoteTestData.votesForRestaurant2;

public class RestaurantTestData {

    public static final int RESTAURANT1_ID = 1;

    public static final MatcherFactory.Matcher<Restaurant> RESTAURANT_MATCHER = MatcherFactory.usingIgnoringFieldsComparator(Restaurant.class, "meals", "votes");

    public static final Restaurant dickinson = new Restaurant(RESTAURANT1_ID, "DICKINSON");
    public static final Restaurant voltaire = new Restaurant(RESTAURANT1_ID + 1, "VOLTAIRE");
    public static final Restaurant dante = new Restaurant(RESTAURANT1_ID + 2, "DANTE");
    public static final Restaurant chekhov = new Restaurant(RESTAURANT1_ID + 3, "CHEKHOV");
    public static final Restaurant hemingway = new Restaurant(RESTAURANT1_ID + 4, "HEMINGWAY");

    public static final List<Restaurant> restaurants = List.of(chekhov, dante, dickinson, hemingway, voltaire);

    public static MatcherFactory.Matcher<RestaurantTo> RESTAURANT_TO_MATCHER = MatcherFactory.usingIgnoringFieldsComparator(RestaurantTo.class, "meals", "rating");

    public static final MatcherFactory.Matcher<RestaurantTo> RESTAURANT_TO_WITH_VOTES_MATCHER = MatcherFactory.usingIgnoringFieldsComparator(RestaurantTo.class, "meals");

    public static MatcherFactory.Matcher<RestaurantTo> RESTAURANT_TO_WITH_MEALS_MATCHER =
            MatcherFactory.usingAssertions(RestaurantTo.class,
                    (a, e) -> assertThat(a).usingRecursiveComparison().ignoringFields("rating", "meals.restaurant").isEqualTo(e),
                    (a, e) -> {
                        throw new UnsupportedOperationException();
                    });

    static {
        chekhov.setVotes(new ArrayList<>());
        dante.setVotes(new ArrayList<>());
        dickinson.setVotes(votesForRestaurant1);
        hemingway.setVotes(new ArrayList<>());
        voltaire.setVotes(votesForRestaurant2);

        dickinson.setMeals(List.of(meal1, meal2, meal3));
        voltaire.setMeals(List.of(meal4, meal5, meal6));
        dante.setMeals(List.of(meal7, meal8));
        chekhov.setMeals(List.of(meal9, meal10, meal11));
        hemingway.setMeals(List.of(meal12, meal13, meal14));
    }

    public static Restaurant getNew() {
        return new Restaurant(null, "New Restaurant");
    }

    public static Restaurant getUpdated() {
        return new Restaurant(RESTAURANT1_ID, "Updated Restaurant");
    }
}
