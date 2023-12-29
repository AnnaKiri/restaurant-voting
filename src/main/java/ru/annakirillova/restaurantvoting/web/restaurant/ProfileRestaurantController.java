package ru.annakirillova.restaurantvoting.web.restaurant;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.annakirillova.restaurantvoting.repository.datajpa.DataJpaRestaurantRepository;
import ru.annakirillova.restaurantvoting.to.RestaurantTo;
import ru.annakirillova.restaurantvoting.util.RestaurantUtil;

import java.util.List;

@RestController
@RequestMapping(value = ProfileRestaurantController.REST_URL, produces = MediaType.APPLICATION_JSON_VALUE)
@Slf4j
public class ProfileRestaurantController {
    static final String REST_URL = "/profile/restaurants";

    @Autowired
    private DataJpaRestaurantRepository repository;

    @GetMapping("/{restaurantId}/with-meals-and-rating")
    public RestaurantTo getWithMealsAndRating(@PathVariable int restaurantId) {
        log.info("get meals and rating for restaurant {}", restaurantId);
        return RestaurantUtil.createTo(repository.getWithMealsAndVotes(restaurantId));
    }

    @GetMapping("/with-rating")
    public List<RestaurantTo> getAllWithRating() {
        log.info("get rating for restaurants");
        return RestaurantUtil.getTos(repository.getAllWithVotes());
    }
}
