package ru.annakirillova.restaurantvoting.web.vote;

import ru.annakirillova.restaurantvoting.model.Vote;
import ru.annakirillova.restaurantvoting.to.VoteTo;
import ru.annakirillova.restaurantvoting.web.MatcherFactory;

import java.time.LocalDate;
import java.util.List;

import static ru.annakirillova.restaurantvoting.web.restaurant.RestaurantTestData.chekhov;
import static ru.annakirillova.restaurantvoting.web.restaurant.RestaurantTestData.dante;
import static ru.annakirillova.restaurantvoting.web.restaurant.RestaurantTestData.dickinson;
import static ru.annakirillova.restaurantvoting.web.restaurant.RestaurantTestData.hemingway;
import static ru.annakirillova.restaurantvoting.web.restaurant.RestaurantTestData.voltaire;
import static ru.annakirillova.restaurantvoting.web.user.UserTestData.user1;
import static ru.annakirillova.restaurantvoting.web.user.UserTestData.user2;
import static ru.annakirillova.restaurantvoting.web.user.UserTestData.user3;

public class VoteTestData {

    public static final int VOTE1_ID = 1;
    public static final int NOT_FOUND = 100;

    public static final Vote vote1 = new Vote(VOTE1_ID, user1, dickinson, LocalDate.now());
    public static final Vote vote2 = new Vote(VOTE1_ID + 1, user2, dickinson, LocalDate.now());

    public static final Vote newYearVote1 = new Vote(VOTE1_ID + 2, user1, dante, LocalDate.of(2024, 1, 1));
    public static final Vote newYearVote2 = new Vote(VOTE1_ID + 3, user2, hemingway, LocalDate.of(2024, 1, 1));
    public static final Vote newYearVote3 = new Vote(VOTE1_ID + 4, user3, hemingway, LocalDate.of(2024, 1, 1));
    public static final Vote newYearVote4 = new Vote(VOTE1_ID + 5, user3, dickinson, LocalDate.of(2024, 1, 2));

    public static final List<Vote> votesForUser1 = List.of(vote1, newYearVote1);

    public static MatcherFactory.Matcher<VoteTo> VOTE_TO_MATCHER = MatcherFactory.usingEqualsComparator(VoteTo.class);

    public static Vote getNew() {
        return new Vote(user3, chekhov);
    }

    public static VoteTo getUpdated() {
        return new VoteTo(VOTE1_ID, user1.getId(), voltaire.getId(), LocalDate.now());
    }
}
