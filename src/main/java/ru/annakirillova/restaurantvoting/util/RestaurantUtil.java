package ru.annakirillova.restaurantvoting.util;

import lombok.experimental.UtilityClass;
import ru.annakirillova.restaurantvoting.model.Restaurant;
import ru.annakirillova.restaurantvoting.model.Vote;
import ru.annakirillova.restaurantvoting.to.RestaurantTo;

import java.util.Collection;
import java.util.List;

@UtilityClass
public class RestaurantUtil {

    public static RestaurantTo createTo(Restaurant restaurant) {
        List<Vote> votes = restaurant.getVotes();
        return new RestaurantTo(restaurant.getName(), restaurant.getMeals(), votes == null ? null : votes.size());
    }

    public static List<RestaurantTo> getTos(Collection<Restaurant> restaurants) {
        return restaurants.stream().map(RestaurantUtil::createTo).toList();
    }
}
