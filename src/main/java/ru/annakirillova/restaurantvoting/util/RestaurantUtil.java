package ru.annakirillova.restaurantvoting.util;

import lombok.experimental.UtilityClass;
import org.hibernate.Hibernate;
import ru.annakirillova.restaurantvoting.model.Meal;
import ru.annakirillova.restaurantvoting.model.Restaurant;
import ru.annakirillova.restaurantvoting.model.Vote;
import ru.annakirillova.restaurantvoting.to.RestaurantTo;

import java.util.Collection;
import java.util.List;

@UtilityClass
public class RestaurantUtil {

    public static RestaurantTo createTo(Restaurant restaurant) {
        List<Vote> votes = restaurant.getVotes();
        Integer rating = Hibernate.isInitialized(votes) ? votes.size() : null;

        List<Meal> meals = restaurant.getMeals();
        return new RestaurantTo(restaurant.getId(), restaurant.getName(), Hibernate.isInitialized(meals) ? meals : null, rating);
    }

    public static List<RestaurantTo> getTos(Collection<Restaurant> restaurants) {
        return restaurants.stream().map(RestaurantUtil::createTo).toList();
    }
}
