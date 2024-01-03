package ru.annakirillova.restaurantvoting.repository.datajpa;

import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.annakirillova.restaurantvoting.model.Restaurant;
import ru.annakirillova.restaurantvoting.repository.RestaurantRepository;

import java.time.LocalDate;
import java.util.List;

@Repository
public class DataJpaRestaurantRepository {
    private static final Sort SORT_NAME = Sort.by(Sort.Direction.ASC, "name");

    private final RestaurantRepository restaurantRepository;

    public DataJpaRestaurantRepository(RestaurantRepository restaurantRepository) {
        this.restaurantRepository = restaurantRepository;
    }

    @Transactional
    public Restaurant save(Restaurant restaurant) {
        return restaurantRepository.save(restaurant);
    }

    @Transactional
    public boolean delete(int id) {
        return restaurantRepository.delete(id) != 0;
    }

    public List<Restaurant> getAllWithVotesToday() {
        return restaurantRepository.getAllWithVotesByDate(LocalDate.now());
    }

    public List<Restaurant> getAll() {
        return restaurantRepository.findAll(SORT_NAME);
    }

    public Restaurant get(int id) {
        return restaurantRepository.findById(id).orElse(null);
    }

    public Restaurant getWithMealsToday(int id) {
        return restaurantRepository.getWithMealsByDate(id, LocalDate.now());
    }

    public Restaurant getWithVotesToday(int id) {
        return restaurantRepository.getWithVotesByDate(id, LocalDate.now());
    }
}