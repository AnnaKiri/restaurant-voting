package ru.annakirillova.restaurantvoting.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;
import ru.annakirillova.restaurantvoting.error.NotFoundException;
import ru.annakirillova.restaurantvoting.model.Restaurant;
import ru.annakirillova.restaurantvoting.to.RestaurantTo;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Transactional(readOnly = true)
public interface RestaurantRepository extends BaseRepository<Restaurant> {
    @Query("SELECT new ru.annakirillova.restaurantvoting.to.RestaurantTo(r.id, r.name, COUNT(v)) FROM Restaurant r LEFT JOIN r.votes v " +
            "WHERE v.created = :date " +
            "GROUP BY r.id, r.name " +
            "ORDER BY r.name")
    List<RestaurantTo> getRestaurantsWithRatingByDate(@Param("date") LocalDate date);

    @Query("SELECT r FROM Restaurant r LEFT JOIN FETCH r.dishes m WHERE m.availableOn = :date ORDER BY r.name")
    List<Restaurant> getRestaurantsWithDishesByDate(@Param("date") LocalDate date);

    @Query("SELECT r FROM Restaurant r LEFT JOIN FETCH r.dishes m WHERE r.id = :id AND m.availableOn = :date")
    Optional<Restaurant> getWithDishesByDate(@Param("id") int id, @Param("date") LocalDate date);

    @Query("SELECT new ru.annakirillova.restaurantvoting.to.RestaurantTo(r.id, r.name, COUNT(v)) FROM Restaurant r LEFT JOIN r.votes v " +
            "WHERE r.id = :id AND v.created = :date " +
            "GROUP BY r.id, r.name")
    Optional<RestaurantTo> getWithRatingByDate(@Param("id") int id, @Param("date") LocalDate date);

    default void checkExisted(int restaurantId) {
        if (!existsById(restaurantId)) {
            throw new NotFoundException("Restaurant id=" + restaurantId + " is not exist");
        }
    }
}
