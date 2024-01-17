package ru.annakirillova.restaurantvoting.web.dish;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import ru.annakirillova.restaurantvoting.config.DateTimeProvider;
import ru.annakirillova.restaurantvoting.model.Dish;
import ru.annakirillova.restaurantvoting.repository.DishRepository;
import ru.annakirillova.restaurantvoting.repository.RestaurantRepository;
import ru.annakirillova.restaurantvoting.service.DishService;

import java.net.URI;
import java.time.LocalDate;
import java.util.List;

import static ru.annakirillova.restaurantvoting.validation.ValidationUtil.assureIdConsistent;
import static ru.annakirillova.restaurantvoting.validation.ValidationUtil.checkNew;

@RestController
@RequestMapping(value = AdminDishController.REST_URL, produces = MediaType.APPLICATION_JSON_VALUE)
@Slf4j
@AllArgsConstructor
@Transactional(readOnly = true)
public class AdminDishController {
    static final String REST_URL = "/admin/restaurants/{restaurantId}/dishes";

    private final DishService dishService;
    private final RestaurantRepository restaurantRepository;
    private final DishRepository dishRepository;
    private final DateTimeProvider dateTimeProvider;

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    @Transactional
    public ResponseEntity<Dish> createWithLocation(@PathVariable int restaurantId, @Valid @RequestBody Dish dish) {
        checkNew(dish);
        restaurantRepository.checkExisted(restaurantId);
        log.info("create a dish {} for the restaurant {}", dish, restaurantId);
        Dish created = dishService.create(restaurantId, dish);
        URI uriOfNewResource = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path(REST_URL + "/{id}")
                .buildAndExpand(restaurantId, created.getId()).toUri();
        return ResponseEntity.created(uriOfNewResource).body(created);
    }

    @DeleteMapping("/{dishId}")
    @Transactional
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable int restaurantId, @PathVariable int dishId) {
        log.info("delete the dish {}", dishId);
        restaurantRepository.checkExisted(restaurantId);
        Dish dish = dishRepository.getBelonged(restaurantId, dishId);
        dishService.delete(dish);
    }

    @PutMapping(value = "/{dishId}", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Transactional
    public void update(@PathVariable int restaurantId, @Valid @RequestBody Dish dish, @PathVariable int dishId) {
        log.info("update the dish {} for the restaurant {}", dish, restaurantId);
        assureIdConsistent(dish, dishId);
        restaurantRepository.checkExisted(restaurantId);
        dishRepository.getBelonged(restaurantId, dishId);
        dishService.update(restaurantId, dish);
    }

    @GetMapping
    public List<Dish> getAll(@PathVariable int restaurantId) {
        log.info("get all dishes of the restaurant {}", restaurantId);
        restaurantRepository.checkExisted(restaurantId);
        return dishRepository.getAll(restaurantId);
    }

    @GetMapping("/filter")
    public List<Dish> getBetween(@PathVariable int restaurantId,
                                 @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
                                 @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        log.info("get all dishes between dates({} - {}) of the restaurant {}", startDate, endDate, restaurantId);
        restaurantRepository.checkExisted(restaurantId);
        return dishRepository.getDishesBetweenDates(startDate, endDate, restaurantId);
    }

    @GetMapping("/{dishId}")
    public Dish get(@PathVariable int restaurantId, @PathVariable int dishId) {
        log.info("get the dish {} of the restaurant {}", dishId, restaurantId);
        restaurantRepository.checkExisted(restaurantId);
        return dishRepository.getBelonged(restaurantId, dishId);
    }

    @GetMapping("/today")
    public List<Dish> getAllToday(@PathVariable int restaurantId) {
        log.info("get all dishes of the restaurant {}", restaurantId);
        restaurantRepository.checkExisted(restaurantId);
        return dishRepository.getAllByDate(dateTimeProvider.getNowDate(), restaurantId);
    }
}