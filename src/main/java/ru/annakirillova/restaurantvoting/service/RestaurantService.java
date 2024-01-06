package ru.annakirillova.restaurantvoting.service;

import lombok.AllArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.annakirillova.restaurantvoting.model.Restaurant;
import ru.annakirillova.restaurantvoting.model.Vote;
import ru.annakirillova.restaurantvoting.repository.RestaurantRepository;
import ru.annakirillova.restaurantvoting.to.RestaurantTo;
import ru.annakirillova.restaurantvoting.util.RestaurantUtil;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Service
@AllArgsConstructor
public class RestaurantService {
    private static final Sort SORT_NAME = Sort.by(Sort.Direction.ASC, "name");

    private final RestaurantRepository restaurantRepository;

    public Restaurant create(Restaurant restaurant) {
        return restaurantRepository.save(restaurant);
    }

    public void delete(int id) {
        restaurantRepository.delete(id);
    }

    public void update(int id, Restaurant restaurant) {
        restaurant.setId(id);
        restaurantRepository.save(restaurant);
    }

    @Transactional
    public List<RestaurantTo> getAllWithVotesToday() {
        Map<Integer, List<Vote>> votesMap = new LinkedHashMap<>();
        for (Restaurant r : restaurantRepository.getRestaurantsWithVotesByDate(LocalDate.now())) {
            votesMap.put(r.getId(), r.getVotes());
        }
        List<Restaurant> restaurantsWithVotes = getAll();
        for (Restaurant r : restaurantsWithVotes) {
            votesMap.computeIfAbsent(r.getId(), id -> new ArrayList<>());
            r.setVotes(votesMap.get(r.getId()));
        }
        return RestaurantUtil.getTos(restaurantsWithVotes);
    }

    public List<Restaurant> getAll() {
        return restaurantRepository.findAll(SORT_NAME);
    }

    public Restaurant getWithMealsToday(int id) {
        return restaurantRepository.getWithMealsByDate(id, LocalDate.now());
    }

    public Restaurant get(int id) {
        return restaurantRepository.findById(id).orElse(null);
    }

    public Restaurant getWithVotesToday(int id) {
        return restaurantRepository.getWithVotesByDate(id, LocalDate.now());
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
