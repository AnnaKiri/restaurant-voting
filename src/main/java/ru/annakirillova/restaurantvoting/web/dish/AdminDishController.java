package ru.annakirillova.restaurantvoting.web.dish;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import ru.annakirillova.restaurantvoting.model.Dish;
import ru.annakirillova.restaurantvoting.service.DishService;

import java.net.URI;
import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping(value = AdminDishController.REST_URL, produces = MediaType.APPLICATION_JSON_VALUE)
@Slf4j
@AllArgsConstructor
public class AdminDishController {
    static final String REST_URL = "/admin/restaurants/{restaurantId}/dishes";

    private final DishService dishService;

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Dish> createWithLocation(@PathVariable int restaurantId, @Valid @RequestBody Dish dish) {
        log.info("create a dish {} for the restaurant {}", dish, restaurantId);
        Dish created = dishService.create(restaurantId, dish);
        URI uriOfNewResource = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path(REST_URL + "/{id}")
                .buildAndExpand(restaurantId, created.getId()).toUri();
        return ResponseEntity.created(uriOfNewResource).body(created);
    }

    @PostMapping(value = "/add-list", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Dish>> createDishList(@PathVariable int restaurantId, @Valid @RequestBody List<Dish> dishesList) {
        log.info("create dishes for the restaurant {} from a list", restaurantId);
        List<Dish> createdList = dishService.saveList(dishesList, restaurantId);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdList);
    }

    @DeleteMapping("/{dishId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable int restaurantId, @PathVariable int dishId) {
        log.info("delete the dish {}", dishId);
        dishService.delete(restaurantId, dishId);
    }

    @PutMapping(value = "/{dishId}", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void update(@PathVariable int restaurantId, @Valid @RequestBody Dish dish, @PathVariable int dishId) {
        log.info("update the dish {} for the restaurant {}", dish, restaurantId);
        dishService.update(restaurantId, dish, dishId);
    }

    @GetMapping
    public List<Dish> getAll(@PathVariable int restaurantId) {
        log.info("get all dishes of the restaurant {}", restaurantId);
        return dishService.getAll(restaurantId);
    }

    @GetMapping("/filter")
    public List<Dish> getBetween(@PathVariable int restaurantId,
                                 @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
                                 @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        log.info("get all dishes between dates({} - {}) of the restaurant {}", startDate, endDate, restaurantId);
        return dishService.getDishesBetweenDates(startDate, endDate, restaurantId);
    }

    @GetMapping("/{dishId}")
    public Dish get(@PathVariable int restaurantId, @PathVariable int dishId) {
        log.info("get the dish {} of the restaurant {}", dishId, restaurantId);
        return dishService.get(restaurantId, dishId);
    }

    @GetMapping("/today")
    public List<Dish> getAllToday(@PathVariable int restaurantId) {
        log.info("get all dishes of the restaurant {}", restaurantId);
        return dishService.getAllToday(restaurantId);
    }
}