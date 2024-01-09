package ru.annakirillova.restaurantvoting.service;

import lombok.AllArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.annakirillova.restaurantvoting.model.Meal;
import ru.annakirillova.restaurantvoting.model.Restaurant;
import ru.annakirillova.restaurantvoting.repository.MealRepository;
import ru.annakirillova.restaurantvoting.repository.RestaurantRepository;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static ru.annakirillova.restaurantvoting.validation.ValidationUtil.assureIdConsistent;
import static ru.annakirillova.restaurantvoting.validation.ValidationUtil.checkNew;

@Service
@AllArgsConstructor
@Transactional(readOnly = true)
public class MealService {

    private final MealRepository mealRepository;
    private final RestaurantRepository restaurantRepository;

    public Meal get(int restaurantId, int mealId) {
        restaurantRepository.checkExisted(restaurantId);
        return mealRepository.getBelonged(restaurantId, mealId);
    }

    @CacheEvict(value = "restaurantsWithMeals", allEntries = true)
    @Transactional
    public Meal create(int restaurantId, Meal meal) {
        checkNew(meal);
        restaurantRepository.checkExisted(restaurantId);
        meal.setRestaurant(restaurantRepository.getReferenceById(restaurantId));
        return mealRepository.save(meal);
    }

    @CacheEvict(value = "restaurantsWithMeals", allEntries = true)
    @Transactional
    public List<Meal> saveList(List<Meal> meals, int restaurantId) {
        if (meals == null || meals.isEmpty()) {
            return Collections.emptyList();
        }
        restaurantRepository.checkExisted(restaurantId);
        Restaurant restaurant = restaurantRepository.getReferenceById(restaurantId);
        List<Meal> savedMeals = new ArrayList<>();
        for (Meal meal : meals) {
            checkNew(meal);
            meal.setRestaurant(restaurant);
            savedMeals.add(mealRepository.save(meal));
        }
        return savedMeals;
    }

    @CacheEvict(value = "restaurantsWithMeals", allEntries = true)
    @Transactional
    public void delete(int restaurantId, int mealId) {
        restaurantRepository.checkExisted(restaurantId);
        Meal meal = mealRepository.getBelonged(restaurantId, mealId);
        mealRepository.delete(meal);
    }

    @CacheEvict(value = "restaurantsWithMeals", allEntries = true)
    @Transactional
    public void update(int restaurantId, Meal meal, int mealId) {
        assureIdConsistent(meal, mealId);
        restaurantRepository.checkExisted(restaurantId);
        mealRepository.getBelonged(restaurantId, mealId);
        meal.setRestaurant(restaurantRepository.getReferenceById(restaurantId));
        mealRepository.save(meal);
    }

    public List<Meal> getAll(int restaurantId) {
        restaurantRepository.checkExisted(restaurantId);
        return mealRepository.getAll(restaurantId);
    }

    public List<Meal> getMealsBetweenDates(LocalDate startDate, LocalDate endDate, int restaurantId) {
        restaurantRepository.checkExisted(restaurantId);
        return mealRepository.getMealsBetweenDates(startDate, endDate, restaurantId);
    }

    public List<Meal> getAllToday(int restaurantId) {
        restaurantRepository.checkExisted(restaurantId);
        return mealRepository.getAllToday(LocalDate.now(), restaurantId);
    }
}
