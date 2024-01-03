package ru.annakirillova.restaurantvoting.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;
import ru.annakirillova.restaurantvoting.model.Meal;

import java.time.LocalDate;
import java.util.List;

@Transactional(readOnly = true)
public interface MealRepository extends JpaRepository<Meal, Integer> {
    @Modifying
    @Transactional
    @Query("DELETE FROM Meal m WHERE m.id= :id")
    int delete(@Param("id") int id);

    @Query("SELECT m from Meal m WHERE m.restaurant.id = :restaurantId ORDER BY m.date DESC")
    List<Meal> getAll(@Param("restaurantId") int restaurantId);

    @Query("SELECT m from Meal m WHERE m.date = :date AND m.restaurant.id = :restaurantId ORDER BY m.date DESC")
    List<Meal> getAllToday(@Param("date") LocalDate date, @Param("restaurantId") int restaurantId);

    @Query("SELECT m from Meal m WHERE m.date >= :startDate AND m.date <= :endDate AND m.restaurant.id = :restaurantId ORDER BY m.date DESC")
    List<Meal> getMealsBetweenDates(@Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate, @Param("restaurantId") int restaurantId);
}
