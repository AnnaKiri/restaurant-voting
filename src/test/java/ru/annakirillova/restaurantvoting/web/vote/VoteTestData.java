package ru.annakirillova.restaurantvoting.web.vote;

import ru.annakirillova.restaurantvoting.model.Vote;
import ru.annakirillova.restaurantvoting.to.VoteTo;
import ru.annakirillova.restaurantvoting.web.MatcherFactory;

import java.time.LocalDate;
import java.time.Month;
import java.util.List;

import static ru.annakirillova.restaurantvoting.web.restaurant.RestaurantTestData.*;
import static ru.annakirillova.restaurantvoting.web.user.UserTestData.*;

public class VoteTestData {

    public static final int VOTE1_ID = 1;

    public static final MatcherFactory.Matcher<Vote> MEAL_MATCHER = MatcherFactory.usingIgnoringFieldsComparator(Vote.class, "user", "restaurant");

    public static final Vote vote1 = new Vote(VOTE1_ID, user1, dickinson, LocalDate.of(2024, Month.JANUARY, 10));
    public static final Vote vote2 = new Vote(VOTE1_ID + 1, user2, dickinson, LocalDate.of(2024, Month.JANUARY, 10));
    public static final Vote vote3 = new Vote(VOTE1_ID + 2, user3, voltaire, LocalDate.of(2024, Month.JANUARY, 10));

    public static final List<Vote> votesForRestaurant1 = List.of(vote1, vote2);
    public static final List<Vote> votesForRestaurant2 = List.of(vote3);

    public static MatcherFactory.Matcher<VoteTo> RESTAURANT_TO_MATCHER = MatcherFactory.usingEqualsComparator(VoteTo.class);

    public static Vote getNew() {
        return new Vote(user1, chekhov);
    }

    public static Vote getUpdated() {
        return new Vote(user1, hemingway);
    }

}
