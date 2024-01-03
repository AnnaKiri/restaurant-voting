package ru.annakirillova.restaurantvoting.web.vote;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.annotation.*;
import ru.annakirillova.restaurantvoting.repository.datajpa.DataJpaVoteRepository;
import ru.annakirillova.restaurantvoting.to.VoteTo;
import ru.annakirillova.restaurantvoting.util.VoteUtil;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping(value = AdminVoteController.REST_URL, produces = MediaType.APPLICATION_JSON_VALUE)
@Slf4j
public class AdminVoteController {
    static final String REST_URL = "/admin/restaurants/{restaurantId}/votes";

    @Autowired
    private DataJpaVoteRepository repository;

    @DeleteMapping("/{voteId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable int voteId) {
        log.info("delete the vote {}", voteId);
        repository.delete(voteId);
    }

    @GetMapping
    public List<VoteTo> getAllByRestaurant(@PathVariable int restaurantId) {
        log.info("get all votes for the restaurant {}", restaurantId);
        return VoteUtil.getTos(repository.getAllByRestaurant(restaurantId));
    }

    @GetMapping("/filter")
    public List<VoteTo> getBetween(@PathVariable int restaurantId,
                                   @RequestParam @Nullable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
                                   @RequestParam @Nullable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        log.info("get votes between dates({} - {}) for the restaurant {}", startDate, endDate, restaurantId);
        return VoteUtil.getTos(repository.getAllBetweenHalfOpen(startDate, endDate, restaurantId));
    }

    @GetMapping("/today")
    public List<VoteTo> getAllToday(@PathVariable int restaurantId) {
        log.info("get votes for today for the restaurant {}", restaurantId);
        return VoteUtil.getTos(repository.getAllByDate(restaurantId, LocalDate.now()));
    }

    @GetMapping("/{voteId}")
    public VoteTo get(@PathVariable int restaurantId, @PathVariable int voteId) {
        log.info("get the vote {} for the restaurant {}", voteId, restaurantId);
        return VoteUtil.createTo(repository.get(voteId));
    }
}
