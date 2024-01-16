package ru.annakirillova.restaurantvoting.web.restaurant;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.lang.Nullable;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
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

    @GetMapping("/{id}/with-dishes-and-rating")
    @Transactional
    public RestaurantTo getWithDishesAndRating(@PathVariable int id) {
        log.info("get the restaurant {} with dishes and rating", id);
        return restaurantService.getWithDishesAndRating(id);
    }

    @GetMapping
    @Transactional
    public List<RestaurantTo> getAllWithDishesAndRating(@RequestParam @Nullable Boolean dishesToday, @RequestParam @Nullable Boolean ratingToday) {
        boolean isDishesNeeded = dishesToday != null && dishesToday;
        boolean isVotesNeeded = ratingToday != null && ratingToday;
        if (isDishesNeeded && isVotesNeeded) {
            log.info("get restaurants with dishes and rating");
            List<RestaurantTo> restaurantsWithVotes = restaurantService.getAllWithRatingToday();
            List<RestaurantTo> restaurantsWithDishes = restaurantService.getAllWithDishesToday();
            for (int i = 0; i < restaurantsWithVotes.size(); i++) {
                restaurantsWithVotes.get(i).setDishes(restaurantsWithDishes.get(i).getDishes());
            }
            return restaurantsWithVotes;
        } else if (isDishesNeeded) {
            log.info("get all restaurants with dishes today");
            return restaurantService.getAllWithDishesToday();
        } else if (isVotesNeeded) {
            log.info("get all restaurants with rating today");
            return restaurantService.getAllWithRatingToday();
        } else {
            log.info("get all restaurants ");
            return RestaurantUtil.getTos(restaurantRepository.getAll());
        }
    }
}
