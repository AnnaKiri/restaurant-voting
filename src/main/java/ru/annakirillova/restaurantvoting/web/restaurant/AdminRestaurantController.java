package ru.annakirillova.restaurantvoting.web.restaurant;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
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
    public ResponseEntity<Restaurant> createWithLocation(@Valid @RequestBody Restaurant restaurant) {
        log.info("create a restaurant {}", restaurant);
        Restaurant created = repository.save(restaurant);
        URI uriOfNewResource = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path(REST_URL).build().toUri();
        return ResponseEntity.created(uriOfNewResource).body(created);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable int id) {
        log.info("delete the restaurant {}", id);
        repository.delete(id);
    }

    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void update(@PathVariable int id, @Valid @RequestBody Restaurant restaurant) {
        log.info("update the restaurant {} with id={}", restaurant, id);
        restaurant.setId(id);
        repository.save(restaurant);
    }

    @GetMapping
    public List<RestaurantTo> getAllWithVotes(@RequestParam @Nullable Boolean votes) {
        boolean isVotesNeeded = votes != null && votes;
        if (isVotesNeeded) {
            log.info("get all restaurants with votes");
            return RestaurantUtil.getTos(repository.getAllWithVotes());
        } else {
            log.info("get all restaurant");
            return RestaurantUtil.getTos(repository.getAll());
        }
    }

    @GetMapping("/{id}")
    public RestaurantTo getWithMeals(@PathVariable int id, @RequestParam @Nullable Boolean meals) {
        boolean isMealsNeeded = meals != null && meals;
        if (isMealsNeeded) {
            log.info("get the restaurant {} with meals", id);
            return RestaurantUtil.createTo(repository.getWithMeals(id));
        } else {
            log.info("get the restaurant {}", id);
            return RestaurantUtil.createTo(repository.get(id));
        }

    }
}
