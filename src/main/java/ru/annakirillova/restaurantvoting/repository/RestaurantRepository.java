package ru.annakirillova.restaurantvoting.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;
import ru.annakirillova.restaurantvoting.model.Restaurant;

import java.time.LocalDate;
import java.util.List;

@Transactional(readOnly = true)
public interface RestaurantRepository extends JpaRepository<Restaurant, Integer> {
    @Transactional
    @Modifying
    @Query("DELETE FROM Restaurant r WHERE r.id =: id")
    int delete(@Param("id") int id);

    @Query("SELECT r FROM Restaurant r LEFT JOIN FETCH r.votes v WHERE v.date = :date")
    List<Restaurant> getAllWithVotesByDate(@Param("date") LocalDate date);

    @Query("SELECT r FROM Restaurant r LEFT JOIN FETCH r.meals m WHERE r.id =: id AND m.date =: date")
    Restaurant getWithMealsByDate(@Param("id") int id, @Param("date") LocalDate date);

    @Query("SELECT r FROM Restaurant r LEFT JOIN FETCH r.meals m LEFT JOIN FETCH r.votes v WHERE r.id = :id AND m.date = :date AND v.date = :date")
    Restaurant getWithMealsAndVotesByDate(@Param("id") int id, @Param("date") LocalDate date);
}
