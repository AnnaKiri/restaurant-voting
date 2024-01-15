package ru.annakirillova.restaurantvoting.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;
import ru.annakirillova.restaurantvoting.error.DataConflictException;
import ru.annakirillova.restaurantvoting.model.Dish;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Transactional(readOnly = true)
public interface DishRepository extends JpaRepository<Dish, Integer> {
    @Query("SELECT d from Dish d WHERE d.restaurant.id = :restaurantId ORDER BY d.availableOn DESC")
    List<Dish> getAll(@Param("restaurantId") int restaurantId);

    @Query("SELECT d from Dish d WHERE d.availableOn = :date AND d.restaurant.id = :restaurantId ORDER BY d.availableOn DESC")
    List<Dish> getAllToday(@Param("date") LocalDate date, @Param("restaurantId") int restaurantId);

    @Query("SELECT d from Dish d WHERE d.availableOn >= :startDate AND d.availableOn <= :endDate AND d.restaurant.id = :restaurantId ORDER BY d.availableOn DESC")
    List<Dish> getDishesBetweenDates(@Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate, @Param("restaurantId") int restaurantId);

    @Query("SELECT d FROM Dish d WHERE d.id = :dishId and d.restaurant.id = :restaurantId")
    Optional<Dish> get(int restaurantId, int dishId);

    default Dish getBelonged(int restaurantId, int dishId) {
        return get(restaurantId, dishId).orElseThrow(
                () -> new DataConflictException("Dish id=" + dishId + "   is not exist or doesn't belong to Restaurant id=" + restaurantId));
    }
}
