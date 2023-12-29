package ru.annakirillova.restaurantvoting.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;
import ru.annakirillova.restaurantvoting.model.Restaurant;

import java.util.List;

@Transactional(readOnly = true)
public interface RestaurantRepository extends JpaRepository<Restaurant, Integer> {
    @Transactional
    @Modifying
    @Query("DELETE FROM Restaurant r WHERE r.id=:id")
    int delete(@Param("id") int id);

    @Query("SELECT r FROM Restaurant r LEFT JOIN FETCH r.votes")
    List<Restaurant> getAllWithVotes();

    @Query("SELECT r FROM Restaurant r LEFT JOIN FETCH r.meals WHERE r.id=?1")
    Restaurant getWithMeals(int id);

    @Query("SELECT r FROM Restaurant r LEFT JOIN FETCH r.meals, r.votes WHERE r.id=?1")
    Restaurant getWithMealsAndVotes(int id);
}
