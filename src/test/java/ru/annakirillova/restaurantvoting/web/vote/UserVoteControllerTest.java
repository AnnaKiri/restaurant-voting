package ru.annakirillova.restaurantvoting.web.vote;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import ru.annakirillova.restaurantvoting.model.Vote;
import ru.annakirillova.restaurantvoting.repository.datajpa.DataJpaVoteRepository;
import ru.annakirillova.restaurantvoting.to.VoteTo;
import ru.annakirillova.restaurantvoting.util.VoteUtil;
import ru.annakirillova.restaurantvoting.web.AbstractControllerTest;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ru.annakirillova.restaurantvoting.web.restaurant.RestaurantTestData.RESTAURANT1_ID;
import static ru.annakirillova.restaurantvoting.web.user.UserTestData.USER1_MAIL;
import static ru.annakirillova.restaurantvoting.web.user.UserTestData.USER3_MAIL;
import static ru.annakirillova.restaurantvoting.web.vote.VoteTestData.*;

public class UserVoteControllerTest extends AbstractControllerTest {

    @Autowired
    private DataJpaVoteRepository voteRepository;

    @Test
    @WithUserDetails(value = USER3_MAIL)
    void createWithLocation() throws Exception {
        ResultActions action = perform(MockMvcRequestBuilders.post(buildUrlWithRestaurantId(UserVoteController.REST_URL, RESTAURANT1_ID + 3))
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isCreated());

        VoteTo created = VOTE_TO_MATCHER.readFromJson(action);
        int newId = created.id();
        Vote newVote = getNew();
        newVote.setId(newId);
        VoteTo newVoteTo = VoteUtil.createTo(newVote);
        VOTE_TO_MATCHER.assertMatch(created, newVoteTo);
        VOTE_TO_MATCHER.assertMatch(VoteUtil.createTo(voteRepository.get(newId)), newVoteTo);
    }

    @Test
    @WithUserDetails(value = USER1_MAIL)
    @Disabled
    void update() throws Exception {
        ResultActions action = perform(MockMvcRequestBuilders.post(buildUrlWithRestaurantId(UserVoteController.REST_URL, RESTAURANT1_ID + 1))
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isCreated());
        VoteTo created = VOTE_TO_MATCHER.readFromJson(action);
        int newId = created.id();
        Vote updatedVote = getUpdated();
        updatedVote.setId(newId);
        VoteTo newVoteTo = VoteUtil.createTo(updatedVote);
        VOTE_TO_MATCHER.assertMatch(created, newVoteTo);
        VOTE_TO_MATCHER.assertMatch(VoteUtil.createTo(voteRepository.get(newId)), newVoteTo);
    }
}
