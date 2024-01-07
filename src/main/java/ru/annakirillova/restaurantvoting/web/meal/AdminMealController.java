package ru.annakirillova.restaurantvoting.web.meal;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import ru.annakirillova.restaurantvoting.model.Meal;
import ru.annakirillova.restaurantvoting.service.MealService;

import java.net.URI;
import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping(value = AdminMealController.REST_URL, produces = MediaType.APPLICATION_JSON_VALUE)
@Slf4j
@AllArgsConstructor
public class AdminMealController {
    static final String REST_URL = "/admin/restaurants/{restaurantId}/meals";

    @Autowired
    private final MealService mealService;

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Meal> createWithLocation(@PathVariable int restaurantId, @Valid @RequestBody Meal meal) {
        log.info("create a meal {} for the restaurant {}", meal, restaurantId);
        Meal created = mealService.create(restaurantId, meal);
        URI uriOfNewResource = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path(REST_URL + "/{id}")
                .buildAndExpand(restaurantId, created.getId()).toUri();
        return ResponseEntity.created(uriOfNewResource).body(created);
    }

    @PostMapping(value = "/add-list", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Meal>> createMealList(@PathVariable int restaurantId, @Valid @RequestBody List<Meal> mealsList) {
        log.info("create meals for the restaurant {} from a list", restaurantId);
        List<Meal> createdList = mealService.saveList(mealsList, restaurantId);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdList);
    }

    @DeleteMapping("/{mealId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable int mealId) {
        log.info("delete the meal {}", mealId);
        mealService.delete(mealId);
    }

    @PutMapping(value = "/{mealId}", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void update(@PathVariable int restaurantId, @Valid @RequestBody Meal meal, @PathVariable int mealId) {
        log.info("update the meal {} for the restaurant {}", meal, restaurantId);
        mealService.update(restaurantId, meal, mealId);
    }

    @GetMapping
    public List<Meal> getAll(@PathVariable int restaurantId) {
        log.info("get all meals of the restaurant {}", restaurantId);
        return mealService.getAll(restaurantId);
    }

    @GetMapping("/filter")
    public List<Meal> getBetween(@PathVariable int restaurantId,
                                 @RequestParam @Nullable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
                                 @RequestParam @Nullable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        log.info("get all meals between dates({} - {}) of the restaurant {}", startDate, endDate, restaurantId);
        return mealService.getMealsBetweenDates(startDate, endDate, restaurantId);
    }

    @GetMapping("/{mealId}")
    public Meal get(@PathVariable int restaurantId, @PathVariable int mealId) {
        log.info("get the meal {} of the restaurant {}", mealId, restaurantId);
        return mealService.get(mealId);
    }

    @GetMapping("/today")
    public List<Meal> getAllToday(@PathVariable int restaurantId) {
        log.info("get all meals of the restaurant {}", restaurantId);
        return mealService.getAllToday(restaurantId);
    }
}