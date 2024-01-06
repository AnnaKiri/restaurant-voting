package ru.annakirillova.restaurantvoting.web.restaurant;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.annakirillova.restaurantvoting.service.RestaurantService;
import ru.annakirillova.restaurantvoting.to.RestaurantTo;

import java.util.List;

@RestController
@RequestMapping(value = UserRestaurantController.REST_URL, produces = MediaType.APPLICATION_JSON_VALUE)
@Slf4j
public class UserRestaurantController {
    static final String REST_URL = "/user/restaurants";

    @Autowired
    private RestaurantService restaurantService;

    @GetMapping("/{id}/with-meals-and-rating")
    @Transactional
    public RestaurantTo getWithMealsAndRating(@PathVariable int id) {
        log.info("get the restaurant {} with meals and rating", id);
        return restaurantService.getWithMealsAndRating(id);
    }

    @GetMapping("/with-rating")
    public List<RestaurantTo> getAllWithRating() {
        log.info("get restaurants with rating");
        return restaurantService.getAllWithVotesToday();
    }
}
