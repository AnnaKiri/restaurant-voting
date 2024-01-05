package ru.annakirillova.restaurantvoting.web.vote;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import ru.annakirillova.restaurantvoting.model.Vote;
import ru.annakirillova.restaurantvoting.repository.datajpa.DataJpaVoteRepository;
import ru.annakirillova.restaurantvoting.to.VoteTo;
import ru.annakirillova.restaurantvoting.util.VoteUtil;
import ru.annakirillova.restaurantvoting.web.AuthUser;

import java.net.URI;

@RestController
@RequestMapping(value = UserVoteController.REST_URL, produces = MediaType.APPLICATION_JSON_VALUE)
@Slf4j
public class UserVoteController {
    static final String REST_URL = "/user/restaurants/{restaurantId}/votes";

    @Autowired
    private DataJpaVoteRepository repository;

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<VoteTo> register(@PathVariable int restaurantId, @AuthenticationPrincipal AuthUser authUser) {
        log.info("the user {} votes for the restaurant {}", authUser.getUser().id(), restaurantId);
        Vote created = repository.save(restaurantId, authUser.id());
        URI uriOfNewResource = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path(REST_URL + "/{id}")
                .buildAndExpand(restaurantId, created.getId()).toUri();
        return ResponseEntity.created(uriOfNewResource).body(VoteUtil.createTo(created));
    }
}
