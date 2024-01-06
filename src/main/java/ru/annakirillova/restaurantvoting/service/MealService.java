package ru.annakirillova.restaurantvoting.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.annakirillova.restaurantvoting.model.Meal;
import ru.annakirillova.restaurantvoting.model.Restaurant;
import ru.annakirillova.restaurantvoting.repository.MealRepository;
import ru.annakirillova.restaurantvoting.repository.RestaurantRepository;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static ru.annakirillova.restaurantvoting.validation.ValidationUtil.assureIdConsistent;
import static ru.annakirillova.restaurantvoting.validation.ValidationUtil.checkNew;

@Service
@AllArgsConstructor
public class MealService {

    private final MealRepository mealRepository;
    private final RestaurantRepository restaurantRepository;

    public Optional<Meal> get(int mealId) {
        return mealRepository.findById(mealId);
    }

    @Transactional
    public Meal create(int restaurantId, Meal meal) {
        checkNew(meal);
        meal.setRestaurant(restaurantRepository.getReferenceById(restaurantId));
        return mealRepository.save(meal);
    }

    @Transactional
    public List<Meal> saveList(List<Meal> meals, int restaurantId) {
        List<Meal> savedMeals = new ArrayList<>();
        Restaurant restaurant = restaurantRepository.getReferenceById(restaurantId);
        for (int i = 0; i < meals.size(); i++) {
            Meal meal = meals.get(i);
            checkNew(meal);
            meal.setRestaurant(restaurant);
            savedMeals.add(mealRepository.save(meal));
        }
        return savedMeals;
    }

    public void delete(int mealId) {
        mealRepository.delete(mealId);
    }

    @Transactional
    public void update(int restaurantId, Meal meal, int mealId) {
        assureIdConsistent(meal, mealId);
        create(restaurantId, meal);
    }

    public List<Meal> getAll(int restaurantId) {
        return mealRepository.getAll(restaurantId);
    }

    public List<Meal> getMealsBetweenDates(LocalDate startDate, LocalDate endDate, int restaurantId) {
        return mealRepository.getMealsBetweenDates(startDate, endDate, restaurantId);
    }

    public List<Meal> getAllToday(int restaurantId) {
        return mealRepository.getAllToday(LocalDate.now(), restaurantId);
    }
}
