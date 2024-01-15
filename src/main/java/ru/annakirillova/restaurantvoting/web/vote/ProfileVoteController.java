package ru.annakirillova.restaurantvoting.web.vote;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import ru.annakirillova.restaurantvoting.model.Vote;
import ru.annakirillova.restaurantvoting.repository.VoteRepository;
import ru.annakirillova.restaurantvoting.service.VoteService;
import ru.annakirillova.restaurantvoting.to.VoteTo;
import ru.annakirillova.restaurantvoting.util.VoteUtil;
import ru.annakirillova.restaurantvoting.validation.ValidationUtil;
import ru.annakirillova.restaurantvoting.web.AuthUser;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping(value = ProfileVoteController.REST_URL, produces = MediaType.APPLICATION_JSON_VALUE)
@Slf4j
@AllArgsConstructor
public class ProfileVoteController {
    static final String REST_URL = "/profile/votes";

    private VoteService voteService;
    private VoteRepository voteRepository;

    @PostMapping()
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<VoteTo> register(@Valid @RequestBody VoteTo voteTo, @AuthenticationPrincipal AuthUser authUser) {
        log.info("the user {} votes for the restaurant {}", authUser.id(), voteTo.getRestaurantId());
        ValidationUtil.checkNew(voteTo);
        Vote created = voteService.save(voteTo.getRestaurantId(), authUser.id());
        URI uriOfNewResource = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path(REST_URL + "/{id}")
                .buildAndExpand(created.getId()).toUri();
        return ResponseEntity.created(uriOfNewResource).body(VoteUtil.createTo(created));
    }

    @PutMapping(value = "/{voteId}", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void update(@Valid @RequestBody VoteTo voteTo, @PathVariable int voteId, @AuthenticationPrincipal AuthUser authUser) {
        ValidationUtil.assureIdConsistent(voteTo, voteId);
        log.info("update the vote with id={}", voteTo.getId());
        voteService.update(voteTo, authUser.id());
    }

    @GetMapping("/{voteId}")
    public VoteTo get(@AuthenticationPrincipal AuthUser authUser, @PathVariable int voteId) {
        int userId = authUser.getUser().id();
        log.info("get a vote for user with id {}", userId);
        return VoteUtil.createTo(voteRepository.getBelonged(userId, voteId));
    }

    @GetMapping("/today")
    public VoteTo getToday(@AuthenticationPrincipal AuthUser authUser) {
        int userId = authUser.getUser().id();
        log.info("get a vote for user with id {} today", userId);
        return VoteUtil.createTo(voteService.getVoteToday(userId));
    }

    @GetMapping
    public List<VoteTo> getAll(@AuthenticationPrincipal AuthUser authUser) {
        int userId = authUser.getUser().id();
        log.info("get all votes for user with id {}", userId);
        return VoteUtil.getTos(voteService.getAll(userId));
    }
}
