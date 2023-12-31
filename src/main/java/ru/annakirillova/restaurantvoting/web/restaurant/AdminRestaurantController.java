package ru.annakirillova.restaurantvoting.web.restaurant;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import ru.annakirillova.restaurantvoting.model.Restaurant;
import ru.annakirillova.restaurantvoting.repository.RestaurantRepository;
import ru.annakirillova.restaurantvoting.service.RestaurantService;
import ru.annakirillova.restaurantvoting.to.RestaurantTo;
import ru.annakirillova.restaurantvoting.util.RestaurantUtil;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping(value = AdminRestaurantController.REST_URL, produces = MediaType.APPLICATION_JSON_VALUE)
@Slf4j
@AllArgsConstructor
public class AdminRestaurantController {
    static final String REST_URL = "/admin/restaurants";

    private RestaurantService restaurantService;
    private RestaurantRepository restaurantRepository;

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<Restaurant> createWithLocation(@Valid @RequestBody Restaurant restaurant) {
        log.info("create a restaurant {}", restaurant);
        Restaurant created = restaurantService.create(restaurant);
        URI uriOfNewResource = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path(REST_URL).build().toUri();
        return ResponseEntity.created(uriOfNewResource).body(created);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable int id) {
        log.info("delete the restaurant {}", id);
        restaurantService.delete(id);
    }

    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void update(@PathVariable int id, @Valid @RequestBody Restaurant restaurant) {
        log.info("update the restaurant {} with id={}", restaurant, id);
        restaurantService.update(id, restaurant);
    }

    @GetMapping
    public List<RestaurantTo> getAllWithMealsToday(@RequestParam @Nullable Boolean mealsToday) {
        boolean isMealsNeeded = mealsToday != null && mealsToday;
        if (isMealsNeeded) {
            log.info("get all restaurants with meals today");
            return restaurantService.getAllWithMealsToday();
        } else {
            log.info("get all restaurant");
            return RestaurantUtil.getTos(restaurantRepository.getAll());
        }
    }

    @GetMapping("/{id}")
    public RestaurantTo getWithMeals(@PathVariable int id, @RequestParam @Nullable Boolean mealsToday) {
        boolean isMealsNeeded = mealsToday != null && mealsToday;
        if (isMealsNeeded) {
            log.info("get the restaurant {} with meals", id);
            return RestaurantUtil.createTo(restaurantService.getWithMealsToday(id));
        } else {
            log.info("get the restaurant {}", id);
            return RestaurantUtil.createTo(restaurantRepository.getExisted(id));
        }
    }
}
