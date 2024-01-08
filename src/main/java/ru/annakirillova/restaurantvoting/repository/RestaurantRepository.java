package ru.annakirillova.restaurantvoting.repository;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;
import ru.annakirillova.restaurantvoting.error.NotFoundException;
import ru.annakirillova.restaurantvoting.model.Restaurant;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static ru.annakirillova.restaurantvoting.service.RestaurantService.SORT_NAME;

@Transactional(readOnly = true)
public interface RestaurantRepository extends BaseRepository<Restaurant> {
    @Query("SELECT r FROM Restaurant r LEFT JOIN FETCH r.votes v WHERE v.created = :date ORDER BY r.name")
    List<Restaurant> getRestaurantsWithVotesByDate(@Param("date") LocalDate date);

    @Query("SELECT r FROM Restaurant r LEFT JOIN FETCH r.meals v WHERE v.created = :date ORDER BY r.name")
    List<Restaurant> getRestaurantsWithMealsByDate(@Param("date") LocalDate date);

    @Query("SELECT r FROM Restaurant r LEFT JOIN FETCH r.meals m WHERE r.id = :id AND m.created = :date")
    Optional<Restaurant> getWithMealsByDate(@Param("id") int id, @Param("date") LocalDate date);

    @Query("SELECT r FROM Restaurant r LEFT JOIN FETCH r.votes v WHERE r.id = :id AND v.created = :date")
    Optional<Restaurant> getWithVotesByDate(@Param("id") int id, @Param("date") LocalDate date);

    @Cacheable("restaurants")
    default List<Restaurant> getAll() {
        return findAll(SORT_NAME);
    }

    default void checkExisted(int restaurantId) {
        if (!existsById(restaurantId)) {
            throw new NotFoundException("Restaurant id=" + restaurantId + "   is not exist");
        }
    }
}
