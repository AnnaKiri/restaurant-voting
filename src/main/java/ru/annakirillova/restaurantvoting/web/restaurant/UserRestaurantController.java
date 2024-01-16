package ru.annakirillova.restaurantvoting.web.restaurant;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
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
    @Transactional(readOnly = true)
    public RestaurantTo getWithDishesAndRating(@PathVariable int id) {
        log.info("get the restaurant {} with dishes and rating", id);
        RestaurantTo restaurantTo = restaurantService.getWithRatingToday(id);
        restaurantTo.setDishes(restaurantService.getWithDishesToday(id).getDishes());
        return restaurantTo;
    }

    @GetMapping(params = {"dishesToday=true", "ratingToday=true"})
    @Transactional(readOnly = true)
    //the unused params are needed for SwaggerUI
    public List<RestaurantTo> getAllWithDishesAndRating(@RequestParam Boolean dishesToday, @RequestParam Boolean ratingToday) {
        log.info("get restaurants with dishes and rating");
        List<RestaurantTo> restaurantsWithVotes = restaurantService.getAllWithRatingToday();
        List<RestaurantTo> restaurantsWithDishes = restaurantService.getAllWithDishesToday();
        for (int i = 0; i < restaurantsWithVotes.size(); i++) {
            restaurantsWithVotes.get(i).setDishes(restaurantsWithDishes.get(i).getDishes());
        }
        return restaurantsWithVotes;
    }

    @GetMapping(params = "dishesToday=true")
    //the unused param is needed for SwaggerUI
    public List<RestaurantTo> getAllWithDishes(@RequestParam Boolean dishesToday) {
        log.info("get all restaurants with dishes today");
        return restaurantService.getAllWithDishesToday();
    }

    @GetMapping(params = "ratingToday=true")
    //the unused param is needed for SwaggerUI
    public List<RestaurantTo> getAllWithRating(@RequestParam Boolean ratingToday) {
        log.info("get all restaurants with rating today");
        return restaurantService.getAllWithRatingToday();
    }

    @GetMapping
    public List<RestaurantTo> getAll() {
        log.info("get all restaurants ");
        return RestaurantUtil.getTos(restaurantRepository.getAll());
    }
}
