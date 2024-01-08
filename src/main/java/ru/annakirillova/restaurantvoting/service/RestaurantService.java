package ru.annakirillova.restaurantvoting.service;

import lombok.AllArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.annakirillova.restaurantvoting.error.NotFoundException;
import ru.annakirillova.restaurantvoting.model.Meal;
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

    public Restaurant get(int id) {
        return restaurantRepository.findById(id).orElseThrow(() -> new NotFoundException("Entity with id=" + id + " not found"));
    }

    @CacheEvict(value = {"restaurants", "restaurantsWithMeals"}, allEntries = true)
    public Restaurant create(Restaurant restaurant) {
        checkNew(restaurant);
        return restaurantRepository.save(restaurant);
    }

    @CacheEvict(value = {"restaurants", "restaurantsWithMeals"}, allEntries = true)
    public void delete(int id) {
        if (restaurantRepository.delete(id) == 0) {
            throw new NotFoundException("Entity with id=" + id + " not found");
        }
    }

    @CacheEvict(value = {"restaurants", "restaurantsWithMeals"}, allEntries = true)
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

    @Cacheable("restaurantsWithMeals")
    @Transactional
    public List<RestaurantTo> getAllWithMealsToday() {
        Map<Integer, List<Meal>> mealsMap = restaurantRepository.getRestaurantsWithMealsByDate(LocalDate.now())
                .stream()
                .collect(Collectors.toMap(Restaurant::getId, Restaurant::getMeals));

        return restaurantRepository.getAll().stream()
                .peek(r -> r.setMeals(mealsMap.getOrDefault(r.getId(), new ArrayList<>())))
                .map(RestaurantUtil::createTo)
                .collect(Collectors.toList());
    }

    @Transactional
    public Restaurant getWithMealsToday(int id) {
        Restaurant restaurant = get(id);
        List<Meal> mealsToday = restaurantRepository.getWithMealsByDate(id, LocalDate.now())
                .map(Restaurant::getMeals)
                .orElse(Collections.emptyList());
        restaurant.setMeals(mealsToday);
        return restaurant;
    }

    public Restaurant getWithVotesToday(int id) {
        Restaurant restaurant = get(id);
        List<Vote> votesToday = restaurantRepository.getWithVotesByDate(id, LocalDate.now())
                .map(Restaurant::getVotes)
                .orElse(Collections.emptyList());
        restaurant.setVotes(votesToday);
        return restaurant;
    }

    @Transactional
    public RestaurantTo getWithMealsAndRating(int id) {
        Restaurant restaurantWithMeals = getWithMealsToday(id);
        Restaurant restaurantWithVotes = getWithVotesToday(id);
        RestaurantTo restaurantTo = RestaurantUtil.createTo(restaurantWithVotes);
        restaurantTo.setMeals(restaurantWithMeals.getMeals());
        return restaurantTo;
    }
}
