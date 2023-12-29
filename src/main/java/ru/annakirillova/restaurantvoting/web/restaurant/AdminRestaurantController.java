package ru.annakirillova.restaurantvoting.web.restaurant;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import ru.annakirillova.restaurantvoting.model.Restaurant;
import ru.annakirillova.restaurantvoting.repository.datajpa.DataJpaRestaurantRepository;
import ru.annakirillova.restaurantvoting.to.RestaurantTo;
import ru.annakirillova.restaurantvoting.util.RestaurantUtil;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping(value = AdminRestaurantController.REST_URL, produces = MediaType.APPLICATION_JSON_VALUE)
@Slf4j
public class AdminRestaurantController {
    static final String REST_URL = "/admin/restaurants";

    @Autowired
    private DataJpaRestaurantRepository repository;

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<Restaurant> register(@Valid @RequestBody Restaurant restaurant) {
        log.info("create {}", restaurant);
        Restaurant created = repository.save(restaurant);
        URI uriOfNewResource = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path(REST_URL).build().toUri();
        return ResponseEntity.created(uriOfNewResource).body(created);
    }

    @DeleteMapping("/{restaurantId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable int restaurantId) {
        log.info("delete {}", restaurantId);
        repository.delete(restaurantId);
    }

    @PutMapping(value = "/{restaurantId}", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Transactional
    public void update(@PathVariable int restaurantId, @Valid @RequestBody Restaurant restaurant) {
        log.info("update {} with id={}", restaurant, restaurantId);
        restaurant.setId(restaurantId);
        repository.save(restaurant);
    }

    @GetMapping
    public List<RestaurantTo> getAllWithVotes(@RequestParam @Nullable Boolean votes) {
        if (votes == null) {
            log.info("getAll");
            return RestaurantUtil.getTos(repository.getAll());
        } else {
            log.info("getAllWithVotes");
            return RestaurantUtil.getTos(repository.getAllWithVotes());
        }
    }

    @GetMapping("/{restaurantId}")
    public RestaurantTo getWithMeals(@PathVariable int restaurantId, @RequestParam @Nullable Boolean meals) {
        if (meals == null) {
            log.info("get {}", restaurantId);
            return RestaurantUtil.createTo(repository.get(restaurantId));
        } else {
            log.info("getWithMeals {}", restaurantId);
            return RestaurantUtil.createTo(repository.getWithMeals(restaurantId));
        }

    }
}
