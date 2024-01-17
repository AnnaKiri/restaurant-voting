package ru.annakirillova.restaurantvoting.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.annakirillova.restaurantvoting.config.DateTimeProvider;
import ru.annakirillova.restaurantvoting.error.NotFoundException;
import ru.annakirillova.restaurantvoting.error.VoteException;
import ru.annakirillova.restaurantvoting.model.Vote;
import ru.annakirillova.restaurantvoting.repository.RestaurantRepository;
import ru.annakirillova.restaurantvoting.repository.UserRepository;
import ru.annakirillova.restaurantvoting.repository.VoteRepository;
import ru.annakirillova.restaurantvoting.to.VoteTo;

import java.time.LocalTime;
import java.util.Optional;

@Service
@AllArgsConstructor
public class VoteService {
    public static final LocalTime RE_VOTE_DEADLINE = LocalTime.of(11, 0);

    private final VoteRepository voteRepository;
    private final RestaurantRepository restaurantRepository;
    private final UserRepository userRepository;
    private final DateTimeProvider dateTimeProvider;

    public Vote getVoteToday(int userId) {
        return voteRepository.getVoteByDate(userId, dateTimeProvider.getNowDate())
                .orElseThrow(() -> new NotFoundException("Today vote for user with id=" + userId + " not found"));
    }

    @Transactional
    public Vote save(int restaurantId, int userId) {
        Optional<Vote> voteToday = voteRepository.getVoteByDate(userId, dateTimeProvider.getNowDate());
        if (voteToday.isEmpty()) {
            Vote newVote = new Vote(userRepository.getReferenceById(userId), restaurantRepository.getReferenceById(restaurantId));
            return voteRepository.save(newVote);
        } else {
            throw new VoteException("It's prohibited to vote again");
        }
    }

    @Transactional
    public Vote update(VoteTo voteTo, int userId) {
        Vote voteToday = voteRepository.getExistedByDate(userId, dateTimeProvider.getNowDate());
        int restaurantId = voteTo.getRestaurantId();
        restaurantRepository.checkExisted(restaurantId);
        if (!voteTo.getId().equals(voteToday.getId())) {
            throw new VoteException("Allowed to change the today vote only");
        } else if (voteToday.getRestaurant().getId() == restaurantId) {
            throw new VoteException("Voting for the same restaurant");
        } else if (dateTimeProvider.getNowTime().isAfter(RE_VOTE_DEADLINE)) {
            throw new VoteException("It's prohibited to change the vote after " + RE_VOTE_DEADLINE);
        } else {
            voteToday.setRestaurant(restaurantRepository.getReferenceById(restaurantId));
            return voteRepository.save(voteToday);
        }
    }
}
