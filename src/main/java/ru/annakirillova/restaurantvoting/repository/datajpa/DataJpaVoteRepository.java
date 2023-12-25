package ru.annakirillova.restaurantvoting.repository.datajpa;

import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.annakirillova.restaurantvoting.model.Vote;
import ru.annakirillova.restaurantvoting.repository.RestaurantRepository;
import ru.annakirillova.restaurantvoting.repository.UserRepository;
import ru.annakirillova.restaurantvoting.repository.VoteRepository;

import java.time.LocalDate;
import java.util.List;

@Repository
@Transactional(readOnly = true)
public class DataJpaVoteRepository {
    private static final Sort SORT_RESTAURANT_DATE = Sort.by(Sort.Direction.ASC, "restaurant", "date");

    private final VoteRepository voteRepository;
    private final RestaurantRepository restaurantRepository;
    private final UserRepository userRepository;

    public DataJpaVoteRepository(VoteRepository voteRepository, RestaurantRepository restaurantRepository, UserRepository userRepository) {
        this.voteRepository = voteRepository;
        this.restaurantRepository = restaurantRepository;
        this.userRepository = userRepository;
    }

    @Transactional
    public Vote save(Vote vote, int restaurantId, int userId) {
        if (!vote.isNew() && get(vote.id(), userId) == null) {
            return null;
        }
        vote.setRestaurant(restaurantRepository.getReferenceById(restaurantId));
        vote.setUser(userRepository.getReferenceById(userId));
        return voteRepository.save(vote);
    }

    @Transactional
    public boolean delete(int id, int userId) {
        return voteRepository.delete(id, userId) != 0;
    }

    public Vote get(int id, int userId) {
        return voteRepository.findById(id)
                .filter(vote -> vote.getUser().getId() == userId)
                .orElse(null);
    }

    public List<Vote> getAllByRestaurant(int restaurantId) {
        return voteRepository.getAllByRestaurant(restaurantId);
    }

    public List<Vote> getAllByDate(int restaurantId, LocalDate date) {
        return voteRepository.getAllByDate(restaurantId, date);
    }

    public List<Vote> getBetweenHalfOpen(LocalDate startDate, LocalDate endDate) {
        return voteRepository.getBetweenHalfOpen(startDate, endDate);
    }
}