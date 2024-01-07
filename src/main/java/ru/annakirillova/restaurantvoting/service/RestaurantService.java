package ru.annakirillova.restaurantvoting.service;

import lombok.AllArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.annakirillova.restaurantvoting.error.NotFoundException;
import ru.annakirillova.restaurantvoting.model.Restaurant;
import ru.annakirillova.restaurantvoting.model.Vote;
import ru.annakirillova.restaurantvoting.repository.RestaurantRepository;
import ru.annakirillova.restaurantvoting.to.RestaurantTo;
import ru.annakirillova.restaurantvoting.util.RestaurantUtil;

import java.time.LocalDate;
import java.util.*;

import static ru.annakirillova.restaurantvoting.validation.ValidationUtil.assureIdConsistent;
import static ru.annakirillova.restaurantvoting.validation.ValidationUtil.checkNew;

@Service
@AllArgsConstructor
public class RestaurantService {
    private static final Sort SORT_NAME = Sort.by(Sort.Direction.ASC, "name");

    private final RestaurantRepository restaurantRepository;

    public Restaurant create(Restaurant restaurant) {
        checkNew(restaurant);
        return restaurantRepository.save(restaurant);
    }

    public void delete(int id) {
        if (restaurantRepository.delete(id) == 0) {
            throw new NotFoundException("Entity with id=" + id + " not found");
        }
    }

    public void update(int id, Restaurant restaurant) {
        assureIdConsistent(restaurant, id);
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

    @Transactional
    public Restaurant getWithMealsToday(int id) {
        Restaurant restaurant = get(id);
        Optional<Restaurant> restaurantWithMeals = restaurantRepository.getWithMealsByDate(id, LocalDate.now());
        if (restaurantWithMeals.isPresent()) {
            restaurant.setMeals(restaurantWithMeals.get().getMeals());
        } else {
            restaurant.setMeals(new ArrayList<>());
        }
        return restaurant;
    }

    public Restaurant get(int id) {
        return restaurantRepository.findById(id).orElseThrow(() -> new NotFoundException("Entity with id=" + id + " not found"));
    }

    public Restaurant getWithVotesToday(int id) {
        Restaurant restaurant = get(id);
        Optional<Restaurant> restaurantWithVotes = restaurantRepository.getWithVotesByDate(id, LocalDate.now());
        if (restaurantWithVotes.isPresent()) {
            restaurant.setVotes(restaurantWithVotes.get().getVotes());
        } else {
            restaurant.setVotes(new ArrayList<>());
        }
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
