package ru.annakirillova.restaurantvoting.web.restaurant;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.annakirillova.restaurantvoting.model.Restaurant;
import ru.annakirillova.restaurantvoting.repository.datajpa.DataJpaRestaurantRepository;
import ru.annakirillova.restaurantvoting.to.RestaurantTo;
import ru.annakirillova.restaurantvoting.util.RestaurantUtil;

import java.util.List;

@RestController
@RequestMapping(value = UserRestaurantController.REST_URL, produces = MediaType.APPLICATION_JSON_VALUE)
@Slf4j
public class UserRestaurantController {
    static final String REST_URL = "/user/restaurants";

    @Autowired
    private DataJpaRestaurantRepository repository;

    @GetMapping("/{id}/with-meals-and-rating")
    @Transactional
    public RestaurantTo getWithMealsAndRating(@PathVariable int id) {
        log.info("get the restaurant {} with meals and rating", id);
        Restaurant restaurantWithMeals = repository.getWithMealsToday(id);
        Restaurant restaurantWithVotes = repository.getWithVotesToday(id);
        RestaurantTo restaurantTo = RestaurantUtil.createTo(restaurantWithVotes);
        restaurantTo.setMeals(restaurantWithMeals.getMeals());
        return restaurantTo;
    }

    @GetMapping("/with-rating")
    public List<RestaurantTo> getAllWithRating() {
        log.info("get restaurants with rating");
        return RestaurantUtil.getTos(repository.getAllWithVotesToday());
    }
}
