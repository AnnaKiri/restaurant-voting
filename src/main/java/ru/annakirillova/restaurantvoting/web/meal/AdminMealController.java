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
import ru.annakirillova.restaurantvoting.repository.datajpa.DataJpaMealRepository;

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
    private final DataJpaMealRepository repository;

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Meal> createWithLocation(@PathVariable int restaurantId, @Valid @RequestBody Meal meal) {
        log.info("create {} for restaurant {}", meal, restaurantId);
        Meal created = repository.save(meal, restaurantId);
        URI uriOfNewResource = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path(REST_URL + "/{id}")
                .buildAndExpand(created.getId()).toUri();
        return ResponseEntity.created(uriOfNewResource).body(created);
    }

    @PostMapping(value = "/add-list", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Meal>> createMealList(@PathVariable int restaurantId, @Valid @RequestBody List<Meal> mealsList) {
        log.info("create list of meals for restaurant {}", restaurantId);
        List<Meal> createdList = repository.saveList(mealsList, restaurantId);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdList);
    }

    @DeleteMapping("/{mealId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable int mealId) {
        log.info("delete {}", mealId);
        repository.delete(mealId);
    }

    @PutMapping(value = "/{mealId}", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void update(@PathVariable int restaurantId, @Valid @RequestBody Meal meal, @PathVariable int mealId) {
        log.info("update {} for restaurant {}", meal, restaurantId);
        meal.setId(mealId);
        repository.save(meal, restaurantId);
    }

    @GetMapping
    public List<Meal> getAll(@PathVariable int restaurantId) {
        log.info("getAll for restaurant {}", restaurantId);
        return repository.getAll(restaurantId);
    }

    @GetMapping("/filter")
    public List<Meal> getBetween(@PathVariable int restaurantId,
                                 @RequestParam @Nullable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
                                 @RequestParam @Nullable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        log.info("getBetween dates({} - {}) for restaurant {}", startDate, endDate, restaurantId);
        return repository.getBetweenHalfOpen(startDate, endDate, restaurantId);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Meal> get(@PathVariable int restaurantId, @PathVariable int id) {
        log.info("get meal {} for restaurant {}", id, restaurantId);
        return ResponseEntity.of(repository.get(id));
    }

    @GetMapping("/today")
    public List<Meal> getAllToday(@PathVariable int restaurantId) {
        log.info("getAllToday for restaurant {}", restaurantId);
        return repository.getAllToday(restaurantId);
    }
}