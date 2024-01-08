package ru.annakirillova.restaurantvoting.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.annakirillova.restaurantvoting.error.NotFoundException;
import ru.annakirillova.restaurantvoting.error.VoteException;
import ru.annakirillova.restaurantvoting.model.Vote;
import ru.annakirillova.restaurantvoting.repository.RestaurantRepository;
import ru.annakirillova.restaurantvoting.repository.UserRepository;
import ru.annakirillova.restaurantvoting.repository.VoteRepository;

import java.time.Clock;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class VoteService {
    public static final LocalTime RE_VOTE_DEADLINE = LocalTime.of(11, 0);

    private final VoteRepository voteRepository;
    private final RestaurantRepository restaurantRepository;
    private final UserRepository userRepository;
    private final Clock clock;

    public Vote get(int voteId) {
        return voteRepository.findById(voteId).orElseThrow(() -> new NotFoundException("Entity with id=" + voteId + " not found"));
    }

    public void delete(int voteId) {
        if (voteRepository.delete(voteId) == 0) {
            throw new NotFoundException("Entity with id=" + voteId + " not found");
        }
    }

    public List<Vote> getAllByRestaurant(int restaurantId) {
        return voteRepository.getAllByRestaurant(restaurantId);
    }

    public List<Vote> getBetween(LocalDate startDate, LocalDate endDate, int restaurantId) {
        return voteRepository.getVotesBetweenDates(startDate, endDate, restaurantId);
    }

    public List<Vote> getAllToday(int restaurantId) {
        return voteRepository.getAllByDate(restaurantId, LocalDate.now());
    }

    @Transactional
    public Vote save(int restaurantId, int userId) {
        Optional<Vote> voteToday = voteRepository.getVoteByDate(userId, LocalDate.now());
        if (voteToday.isEmpty()) {
            Vote newVote = new Vote(userRepository.getReferenceById(userId), restaurantRepository.getReferenceById(restaurantId));
            return voteRepository.save(newVote);
        } else {
            Vote newVote = voteToday.get();
            if (newVote.getRestaurant().getId() == restaurantId) {
                throw new VoteException("Voting for the same restaurant");
            } else if (LocalTime.now(clock).isAfter(RE_VOTE_DEADLINE)) {
                throw new VoteException("It's prohibited to change the vote after " + RE_VOTE_DEADLINE);
            } else {
                newVote.setRestaurant(restaurantRepository.getReferenceById(restaurantId));
                return voteRepository.save(newVote);
            }
        }
    }
}
