package ru.annakirillova.restaurantvoting.web.restaurant;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.lang.Nullable;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import ru.annakirillova.restaurantvoting.repository.RestaurantRepository;
import ru.annakirillova.restaurantvoting.service.RestaurantService;
import ru.annakirillova.restaurantvoting.to.RestaurantTo;
import ru.annakirillova.restaurantvoting.util.RestaurantUtil;

import java.util.List;

@RestController
@RequestMapping(value = UserRestaurantController.REST_URL, produces = MediaType.APPLICATION_JSON_VALUE)
@Slf4j
@AllArgsConstructor
public class UserRestaurantController {
    static final String REST_URL = "/user/restaurants";

    private RestaurantService restaurantService;
    private RestaurantRepository restaurantRepository;

    @GetMapping("/{id}/with-meals-and-rating")
    @Transactional
    public RestaurantTo getWithMealsAndRating(@PathVariable int id) {
        log.info("get the restaurant {} with meals and rating", id);
        return restaurantService.getWithMealsAndRating(id);
    }

    @GetMapping
    @Transactional
    public List<RestaurantTo> getAllWithMealsAndRating(@RequestParam @Nullable Boolean mealsToday, @RequestParam @Nullable Boolean ratingToday) {
        boolean isMealsNeeded = mealsToday != null && mealsToday;
        boolean isVotesNeeded = ratingToday != null && ratingToday;
        if (isMealsNeeded && isVotesNeeded) {
            log.info("get restaurants with meals and rating");
            List<RestaurantTo> restaurantsWithVotes = restaurantService.getAllWithVotesToday();
            List<RestaurantTo> restaurantsWithMeals = restaurantService.getAllWithMealsToday();
            for (int i = 0; i < restaurantsWithVotes.size(); i++) {
                restaurantsWithVotes.get(i).setMeals(restaurantsWithMeals.get(i).getMeals());
            }
            return restaurantsWithMeals;
        } else if (isMealsNeeded) {
            log.info("get all restaurants with meals today");
            return restaurantService.getAllWithMealsToday();
        } else if (isVotesNeeded) {
            log.info("get all restaurants with rating today");
            return restaurantService.getAllWithVotesToday();
        } else {
            log.info("get all restaurants ");
            return RestaurantUtil.getTos(restaurantRepository.getAll());
        }
    }
}
