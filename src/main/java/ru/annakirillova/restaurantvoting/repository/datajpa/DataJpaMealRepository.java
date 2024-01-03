package ru.annakirillova.restaurantvoting.repository.datajpa;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.annakirillova.restaurantvoting.model.Meal;
import ru.annakirillova.restaurantvoting.model.Restaurant;
import ru.annakirillova.restaurantvoting.repository.MealRepository;
import ru.annakirillova.restaurantvoting.repository.RestaurantRepository;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
public class DataJpaMealRepository {

    private final MealRepository mealRepository;
    private final RestaurantRepository restaurantRepository;

    public DataJpaMealRepository(MealRepository mealRepository, RestaurantRepository restaurantRepository) {
        this.mealRepository = mealRepository;
        this.restaurantRepository = restaurantRepository;
    }

    @Transactional
    public Meal save(Meal meal, int restaurantId) {
        if (!meal.isNew() && get(meal.id()).isEmpty()) {
            return null;
        }
        meal.setRestaurant(restaurantRepository.getReferenceById(restaurantId));
        Meal savedMeal = mealRepository.save(meal);
        return savedMeal;
    }

    @Transactional
    public List<Meal> saveList(List<Meal> meals, int restaurantId) {
        List<Meal> savedMeals = new ArrayList<>();
        Restaurant restaurant = restaurantRepository.getReferenceById(restaurantId);
        for (int i = 0; i < meals.size(); i++) {
            Meal meal = meals.get(i);
            meal.setRestaurant(restaurant);
            savedMeals.add(mealRepository.save(meal));
        }
        return savedMeals;
    }

    @Transactional
    public boolean delete(int id) {
        return mealRepository.delete(id) != 0;
    }

    public List<Meal> getAll(int restaurantId) {
        return mealRepository.getAll(restaurantId);
    }

    public List<Meal> getMealsBetweenDates(LocalDate startDate, LocalDate endDate, int restaurantId) {
        return mealRepository.getMealsBetweenDates(startDate, endDate, restaurantId);
    }

    public Optional<Meal> get(int id) {
        return mealRepository.findById(id);
    }

    public List<Meal> getAllToday(int restaurantId) {
        return mealRepository.getAllToday(LocalDate.now(), restaurantId);
    }
}