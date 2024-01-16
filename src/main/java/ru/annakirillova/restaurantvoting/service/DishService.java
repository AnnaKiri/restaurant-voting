package ru.annakirillova.restaurantvoting.service;

import lombok.AllArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;
import ru.annakirillova.restaurantvoting.model.Dish;
import ru.annakirillova.restaurantvoting.repository.DishRepository;
import ru.annakirillova.restaurantvoting.repository.RestaurantRepository;

@Service
@AllArgsConstructor
public class DishService {

    private final DishRepository dishRepository;
    private final RestaurantRepository restaurantRepository;

    @CacheEvict(value = "restaurantsWithDishes", allEntries = true)
    public Dish create(int restaurantId, Dish dish) {
        dish.setRestaurant(restaurantRepository.getReferenceById(restaurantId));
        return dishRepository.save(dish);
    }

    @CacheEvict(value = "restaurantsWithDishes", allEntries = true)
    public void delete(Dish dish) {
        dishRepository.delete(dish);
    }

    @CacheEvict(value = "restaurantsWithDishes", allEntries = true)
    public void update(int restaurantId, Dish dish) {
        dish.setRestaurant(restaurantRepository.getReferenceById(restaurantId));
        dishRepository.save(dish);
    }
}
