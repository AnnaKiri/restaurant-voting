package ru.annakirillova.restaurantvoting.web.restaurant;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.lang.Nullable;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import ru.annakirillova.restaurantvoting.service.RestaurantService;
import ru.annakirillova.restaurantvoting.to.RestaurantTo;
import ru.annakirillova.restaurantvoting.util.RestaurantUtil;

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

    @GetMapping
    public List<RestaurantTo> getAllWithMealsAndRating(@RequestParam @Nullable Boolean meals, @RequestParam @Nullable Boolean votes) {
        boolean isMealsNeeded = meals != null && meals;
        boolean isVotesNeeded = votes != null && votes;
        if (isMealsNeeded && isVotesNeeded) {
            log.info("get restaurants with meals and rating");
            return restaurantService.getAllWithVotesAndMealsToday();
        } else if (isMealsNeeded) {
            log.info("get all restaurants with meals today");
            return restaurantService.getAllWithMealsToday();
        } else if (isVotesNeeded) {
            log.info("get all restaurants with votes today");
            return restaurantService.getAllWithVotesToday();
        } else {
            log.info("get all restaurants ");
            return RestaurantUtil.getTos(restaurantService.getAll());
        }
    }
}
