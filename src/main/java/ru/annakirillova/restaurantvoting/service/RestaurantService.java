package ru.annakirillova.restaurantvoting.service;

import lombok.AllArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.annakirillova.restaurantvoting.config.DateTimeProvider;
import ru.annakirillova.restaurantvoting.model.Dish;
import ru.annakirillova.restaurantvoting.model.Restaurant;
import ru.annakirillova.restaurantvoting.repository.RestaurantRepository;
import ru.annakirillova.restaurantvoting.to.RestaurantTo;
import ru.annakirillova.restaurantvoting.util.RestaurantUtil;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class RestaurantService {
    public static final Sort SORT_NAME = Sort.by(Sort.Direction.ASC, "name");

    private final RestaurantRepository restaurantRepository;
    private final DateTimeProvider dateTimeProvider;

    @CacheEvict(value = {"restaurants", "restaurantsWithDishes"}, allEntries = true)
    public Restaurant create(Restaurant restaurant) {
        return restaurantRepository.save(restaurant);
    }

    @CacheEvict(value = {"restaurants", "restaurantsWithDishes"}, allEntries = true)
    public void delete(int id) {
        restaurantRepository.deleteExisted(id);
    }

    @CacheEvict(value = {"restaurants", "restaurantsWithDishes"}, allEntries = true)
    public void update(int id, Restaurant restaurant) {
        restaurantRepository.save(restaurant);
    }

    @Transactional(readOnly = true)
    public List<RestaurantTo> getAllWithRatingToday() {
        Map<Integer, Integer> ratingMap = restaurantRepository.getRestaurantsWithRatingByDate(dateTimeProvider.getNowDate())
                .stream()
                .collect(Collectors.toMap(RestaurantTo::getId, RestaurantTo::getRating));
        return getAll().stream()
                .map(RestaurantUtil::createTo)
                .peek(r -> r.setRating(ratingMap.getOrDefault(r.getId(), 0)))
                .collect(Collectors.toList());
    }

    @Cacheable("restaurantsWithDishes")
    @Transactional(readOnly = true)
    public List<RestaurantTo> getAllWithDishesToday() {
        Map<Integer, List<Dish>> dishesMap = restaurantRepository.getRestaurantsWithDishesByDate(dateTimeProvider.getNowDate())
                .stream()
                .collect(Collectors.toMap(Restaurant::getId, Restaurant::getDishes));
        return getAll().stream()
                .peek(r -> r.setDishes(dishesMap.getOrDefault(r.getId(), new ArrayList<>())))
                .map(RestaurantUtil::createTo)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public Restaurant getWithDishesToday(int id) {
        Restaurant restaurant = restaurantRepository.getExisted(id);
        List<Dish> dishesToday = restaurantRepository.getWithDishesByDate(id, dateTimeProvider.getNowDate())
                .map(Restaurant::getDishes)
                .orElse(Collections.emptyList());
        restaurant.setDishes(dishesToday);
        return restaurant;
    }

    @Transactional(readOnly = true)
    public RestaurantTo getWithRatingToday(int id) {
        RestaurantTo restaurantTo = RestaurantUtil.createTo(restaurantRepository.getExisted(id));
        int rating = restaurantRepository.getWithRatingByDate(id, dateTimeProvider.getNowDate())
                .map(RestaurantTo::getRating)
                .orElse(0);
        restaurantTo.setRating(rating);
        return restaurantTo;
    }

    @Cacheable("restaurants")
    public List<Restaurant> getAll() {
        return restaurantRepository.findAll(SORT_NAME);
    }
}
