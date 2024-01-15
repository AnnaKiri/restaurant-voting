package ru.annakirillova.restaurantvoting.util;

import lombok.experimental.UtilityClass;
import org.hibernate.Hibernate;
import ru.annakirillova.restaurantvoting.model.Dish;
import ru.annakirillova.restaurantvoting.model.Restaurant;
import ru.annakirillova.restaurantvoting.model.Vote;
import ru.annakirillova.restaurantvoting.to.RestaurantTo;

import java.util.Collection;
import java.util.List;

@UtilityClass
public class RestaurantUtil {

    public static RestaurantTo createTo(Restaurant restaurant) {
        List<Vote> votes = restaurant.getVotes();
        Integer rating = Hibernate.isInitialized(votes) && votes != null ? votes.size() : null;

        List<Dish> dishes = restaurant.getDishes();
        return new RestaurantTo(restaurant.getId(), restaurant.getName(), Hibernate.isInitialized(dishes) ? dishes : null, rating);
    }

    public static List<RestaurantTo> getTos(Collection<Restaurant> restaurants) {
        return restaurants.stream().map(RestaurantUtil::createTo).toList();
    }
}
