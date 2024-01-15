package ru.annakirillova.restaurantvoting.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;
import ru.annakirillova.restaurantvoting.error.DataConflictException;
import ru.annakirillova.restaurantvoting.model.Meal;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Transactional(readOnly = true)
public interface MealRepository extends JpaRepository<Meal, Integer> {
    @Query("SELECT m from Meal m WHERE m.restaurant.id = :restaurantId ORDER BY m.availableOn DESC")
    List<Meal> getAll(@Param("restaurantId") int restaurantId);

    @Query("SELECT m from Meal m WHERE m.availableOn = :date AND m.restaurant.id = :restaurantId ORDER BY m.availableOn DESC")
    List<Meal> getAllToday(@Param("date") LocalDate date, @Param("restaurantId") int restaurantId);

    @Query("SELECT m from Meal m WHERE m.availableOn >= :startDate AND m.availableOn <= :endDate AND m.restaurant.id = :restaurantId ORDER BY m.availableOn DESC")
    List<Meal> getMealsBetweenDates(@Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate, @Param("restaurantId") int restaurantId);

    @Query("SELECT m FROM Meal m WHERE m.id = :mealId and m.restaurant.id = :restaurantId")
    Optional<Meal> get(int restaurantId, int mealId);

    default Meal getBelonged(int restaurantId, int mealId) {
        return get(restaurantId, mealId).orElseThrow(
                () -> new DataConflictException("Meal id=" + mealId + "   is not exist or doesn't belong to Restaurant id=" + restaurantId));
    }
}
