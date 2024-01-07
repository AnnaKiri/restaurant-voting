package ru.annakirillova.restaurantvoting.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;
import ru.annakirillova.restaurantvoting.model.Restaurant;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Transactional(readOnly = true)
public interface RestaurantRepository extends JpaRepository<Restaurant, Integer> {
    @Transactional
    @Modifying
    @Query("DELETE FROM Restaurant r WHERE r.id = :id")
    int delete(@Param("id") int id);

    @Query("SELECT r FROM Restaurant r LEFT JOIN FETCH r.votes v WHERE v.date = :date ORDER BY r.name")
    List<Restaurant> getRestaurantsWithVotesByDate(@Param("date") LocalDate date);

    @Query("SELECT r FROM Restaurant r LEFT JOIN FETCH r.meals v WHERE v.date = :date ORDER BY r.name")
    List<Restaurant> getRestaurantsWithMealsByDate(@Param("date") LocalDate date);

    @Query("SELECT r FROM Restaurant r LEFT JOIN FETCH r.meals m WHERE r.id = :id AND m.date = :date")
    Optional<Restaurant> getWithMealsByDate(@Param("id") int id, @Param("date") LocalDate date);

    @Query("SELECT r FROM Restaurant r LEFT JOIN FETCH r.votes v WHERE r.id = :id AND v.date = :date")
    Optional<Restaurant> getWithVotesByDate(@Param("id") int id, @Param("date") LocalDate date);
}
