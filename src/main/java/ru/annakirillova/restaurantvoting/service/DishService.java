package ru.annakirillova.restaurantvoting.service;

import lombok.AllArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.annakirillova.restaurantvoting.model.Dish;
import ru.annakirillova.restaurantvoting.repository.DishRepository;
import ru.annakirillova.restaurantvoting.repository.RestaurantRepository;
import ru.annakirillova.restaurantvoting.config.DateTimeProvider;

import java.time.LocalDate;
import java.util.List;

import static ru.annakirillova.restaurantvoting.validation.ValidationUtil.assureIdConsistent;
import static ru.annakirillova.restaurantvoting.validation.ValidationUtil.checkNew;

@Service
@AllArgsConstructor
@Transactional(readOnly = true)
public class DishService {

    private final DishRepository dishRepository;
    private final RestaurantRepository restaurantRepository;
    private final DateTimeProvider dateTimeProvider;

    public Dish get(int restaurantId, int dishId) {
        restaurantRepository.checkExisted(restaurantId);
        return dishRepository.getBelonged(restaurantId, dishId);
    }

    @CacheEvict(value = "restaurantsWithDishes", allEntries = true)
    @Transactional
    public Dish create(int restaurantId, Dish dish) {
        checkNew(dish);
        restaurantRepository.checkExisted(restaurantId);
        dish.setRestaurant(restaurantRepository.getReferenceById(restaurantId));
        return dishRepository.save(dish);
    }

    @CacheEvict(value = "restaurantsWithDishes", allEntries = true)
    @Transactional
    public void delete(int restaurantId, int dishId) {
        restaurantRepository.checkExisted(restaurantId);
        Dish dish = dishRepository.getBelonged(restaurantId, dishId);
        dishRepository.delete(dish);
    }

    @CacheEvict(value = "restaurantsWithDishes", allEntries = true)
    @Transactional
    public void update(int restaurantId, Dish dish, int dishId) {
        assureIdConsistent(dish, dishId);
        restaurantRepository.checkExisted(restaurantId);
        dishRepository.getBelonged(restaurantId, dishId);
        dish.setRestaurant(restaurantRepository.getReferenceById(restaurantId));
        dishRepository.save(dish);
    }

    public List<Dish> getAll(int restaurantId) {
        restaurantRepository.checkExisted(restaurantId);
        return dishRepository.getAll(restaurantId);
    }

    public List<Dish> getDishesBetweenDates(LocalDate startDate, LocalDate endDate, int restaurantId) {
        restaurantRepository.checkExisted(restaurantId);
        return dishRepository.getDishesBetweenDates(startDate, endDate, restaurantId);
    }

    public List<Dish> getAllToday(int restaurantId) {
        restaurantRepository.checkExisted(restaurantId);
        return dishRepository.getAllToday(dateTimeProvider.getNowDate(), restaurantId);
    }
}
