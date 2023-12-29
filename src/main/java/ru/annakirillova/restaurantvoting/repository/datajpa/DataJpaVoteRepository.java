package ru.annakirillova.restaurantvoting.repository.datajpa;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.annakirillova.restaurantvoting.model.Vote;
import ru.annakirillova.restaurantvoting.repository.RestaurantRepository;
import ru.annakirillova.restaurantvoting.repository.UserRepository;
import ru.annakirillova.restaurantvoting.repository.VoteRepository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

@Repository
@Transactional(readOnly = true)
public class DataJpaVoteRepository {

    private final VoteRepository voteRepository;
    private final RestaurantRepository restaurantRepository;
    private final UserRepository userRepository;

    public DataJpaVoteRepository(VoteRepository voteRepository, RestaurantRepository restaurantRepository, UserRepository userRepository) {
        this.voteRepository = voteRepository;
        this.restaurantRepository = restaurantRepository;
        this.userRepository = userRepository;
    }

    @Transactional
    public Vote save(int restaurantId, int userId) {
        Optional<Vote> voteToday = voteRepository.getVoteByDate(userId, LocalDate.now());
        if (voteToday.isPresent()) {
            if (LocalTime.now().isBefore(LocalTime.of(11, 0))) {
                voteRepository.delete(voteToday.get().getId());
            } else {
                return null;
            }
        }

        Vote newVote = new Vote(userRepository.getReferenceById(userId), restaurantRepository.getReferenceById(restaurantId));
        return voteRepository.save(newVote);
    }

    @Transactional
    public boolean delete(int id) {
        return voteRepository.delete(id) != 0;
    }

    public Optional<Vote> get(int id) {
        return voteRepository.findById(id);
    }

    public List<Vote> getAllByRestaurant(int restaurantId) {
        return voteRepository.getAllByRestaurant(restaurantId);
    }

    public List<Vote> getAllByDate(int restaurantId, LocalDate date) {
        return voteRepository.getAllByDate(restaurantId, date);
    }

    public List<Vote> getBetweenHalfOpen(LocalDate startDate, LocalDate endDate, int restaurantId) {
        return voteRepository.getBetweenHalfOpen(startDate, endDate, restaurantId);
    }
}