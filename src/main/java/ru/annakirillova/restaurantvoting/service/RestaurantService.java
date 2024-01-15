package ru.annakirillova.restaurantvoting.service;

import lombok.AllArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.annakirillova.restaurantvoting.model.Dish;
import ru.annakirillova.restaurantvoting.model.Restaurant;
import ru.annakirillova.restaurantvoting.model.Vote;
import ru.annakirillova.restaurantvoting.repository.RestaurantRepository;
import ru.annakirillova.restaurantvoting.to.RestaurantTo;
import ru.annakirillova.restaurantvoting.util.RestaurantUtil;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static ru.annakirillova.restaurantvoting.validation.ValidationUtil.assureIdConsistent;
import static ru.annakirillova.restaurantvoting.validation.ValidationUtil.checkNew;

@Service
@AllArgsConstructor
public class RestaurantService {
    public static final Sort SORT_NAME = Sort.by(Sort.Direction.ASC, "name");

    private final RestaurantRepository restaurantRepository;

    @CacheEvict(value = {"restaurants", "restaurantsWithDishes"}, allEntries = true)
    public Restaurant create(Restaurant restaurant) {
        checkNew(restaurant);
        return restaurantRepository.save(restaurant);
    }

    @CacheEvict(value = {"restaurants", "restaurantsWithDishes"}, allEntries = true)
    public void delete(int id) {
        restaurantRepository.deleteExisted(id);
    }

    @CacheEvict(value = {"restaurants", "restaurantsWithDishes"}, allEntries = true)
    public void update(int id, Restaurant restaurant) {
        assureIdConsistent(restaurant, id);
        restaurantRepository.save(restaurant);
    }

    @Transactional
    public List<RestaurantTo> getAllWithVotesToday() {
        Map<Integer, List<Vote>> votesMap = restaurantRepository.getRestaurantsWithVotesByDate(LocalDate.now())
                .stream()
                .collect(Collectors.toMap(Restaurant::getId, Restaurant::getVotes));

        return restaurantRepository.getAll().stream()
                .peek(r -> r.setVotes(votesMap.getOrDefault(r.getId(), new ArrayList<>())))
                .map(RestaurantUtil::createTo)
                .collect(Collectors.toList());
    }

    @Cacheable("restaurantsWithDishes")
    @Transactional
    public List<RestaurantTo> getAllWithDishesToday() {
        Map<Integer, List<Dish>> dishesMap = restaurantRepository.getRestaurantsWithDishesByDate(LocalDate.now())
                .stream()
                .collect(Collectors.toMap(Restaurant::getId, Restaurant::getDishes));
        return restaurantRepository.getAll().stream()
                .peek(r -> r.setDishes(dishesMap.getOrDefault(r.getId(), new ArrayList<>())))
                .map(RestaurantUtil::createTo)
                .collect(Collectors.toList());
    }

    @Transactional
    public Restaurant getWithDishesToday(int id) {
        Restaurant restaurant = restaurantRepository.getExisted(id);
        List<Dish> dishesToday = restaurantRepository.getWithDishesByDate(id, LocalDate.now())
                .map(Restaurant::getDishes)
                .orElse(Collections.emptyList());
        restaurant.setDishes(dishesToday);
        return restaurant;
    }

    @Transactional
    public Restaurant getWithVotesToday(int id) {
        Restaurant restaurant = restaurantRepository.getExisted(id);
        List<Vote> votesToday = restaurantRepository.getWithVotesByDate(id, LocalDate.now())
                .map(Restaurant::getVotes)
                .orElse(Collections.emptyList());
        restaurant.setVotes(votesToday);
        return restaurant;
    }

    @Transactional
    public RestaurantTo getWithDishesAndRating(int id) {
        Restaurant restaurantWithDishes = getWithDishesToday(id);
        Restaurant restaurantWithVotes = getWithVotesToday(id);
        RestaurantTo restaurantTo = RestaurantUtil.createTo(restaurantWithVotes);
        restaurantTo.setDishes(restaurantWithDishes.getDishes());
        return restaurantTo;
    }
}
