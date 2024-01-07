package ru.annakirillova.restaurantvoting.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.annakirillova.restaurantvoting.error.NotFoundException;
import ru.annakirillova.restaurantvoting.model.Vote;
import ru.annakirillova.restaurantvoting.repository.RestaurantRepository;
import ru.annakirillova.restaurantvoting.repository.UserRepository;
import ru.annakirillova.restaurantvoting.repository.VoteRepository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class VoteService {

    private final VoteRepository voteRepository;
    private final RestaurantRepository restaurantRepository;
    private final UserRepository userRepository;

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

    public List<Vote> getAllToday(int restaurantId, LocalDate date) {
        return voteRepository.getAllByDate(restaurantId, date);
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
}
