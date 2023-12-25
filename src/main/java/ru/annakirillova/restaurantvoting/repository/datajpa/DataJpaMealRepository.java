package ru.annakirillova.restaurantvoting.repository.datajpa;

import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.annakirillova.restaurantvoting.model.Meal;
import ru.annakirillova.restaurantvoting.model.Restaurant;
import ru.annakirillova.restaurantvoting.repository.MealRepository;
import ru.annakirillova.restaurantvoting.repository.RestaurantRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Repository
public class DataJpaMealRepository {
    private static final Sort SORT_DESCRIPTION = Sort.by(Sort.Direction.ASC, "description");

    private final MealRepository mealRepository;
    private final RestaurantRepository restaurantRepository;

    public DataJpaMealRepository(MealRepository mealRepository, RestaurantRepository restaurantRepository) {
        this.mealRepository = mealRepository;
        this.restaurantRepository = restaurantRepository;
    }

    @Transactional
    public Meal save(Meal meal, int restaurantId) {
        if (!meal.isNew() && get(meal.id(), restaurantId) == null) {
            return null;
        }
        meal.setRestaurant(restaurantRepository.getReferenceById(restaurantId));
        return mealRepository.save(meal);
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
    public boolean delete(int id, int restaurantId) {
        return mealRepository.delete(id, restaurantId) != 0;
    }

    public List<Meal> getAll() {
        return mealRepository.findAll(SORT_DESCRIPTION);
    }

    public List<Meal> getBetweenHalfOpen(LocalDateTime startDate, LocalDateTime endDate, int restaurantId) {
        return mealRepository.getBetweenHalfOpen(startDate, endDate);
    }

    public Meal get(int id, int restaurantId) {
        return mealRepository.findById(id)
                .filter(meal -> meal.getRestaurant().getId() == restaurantId)
                .orElse(null);
    }

    public List<Meal> getAllToday() {
        return mealRepository.getAllToday(LocalDateTime.now());  // нужна только дата
    }
}