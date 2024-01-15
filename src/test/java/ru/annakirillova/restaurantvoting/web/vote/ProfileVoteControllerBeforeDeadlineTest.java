package ru.annakirillova.restaurantvoting.web.vote;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import ru.annakirillova.restaurantvoting.model.Vote;
import ru.annakirillova.restaurantvoting.repository.VoteRepository;
import ru.annakirillova.restaurantvoting.to.VoteTo;
import ru.annakirillova.restaurantvoting.util.JsonUtil;
import ru.annakirillova.restaurantvoting.util.VoteUtil;
import ru.annakirillova.restaurantvoting.web.AbstractControllerTest;
import ru.annakirillova.restaurantvoting.web.restaurant.RestaurantTestData;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ru.annakirillova.restaurantvoting.web.restaurant.RestaurantTestData.RESTAURANT1_ID;
import static ru.annakirillova.restaurantvoting.web.user.UserTestData.USER1_ID;
import static ru.annakirillova.restaurantvoting.web.user.UserTestData.USER1_MAIL;
import static ru.annakirillova.restaurantvoting.web.user.UserTestData.USER3_MAIL;
import static ru.annakirillova.restaurantvoting.web.vote.ProfileVoteController.REST_URL;
import static ru.annakirillova.restaurantvoting.web.vote.VoteTestData.*;

@ActiveProfiles("timeBeforeDeadline")
public class ProfileVoteControllerBeforeDeadlineTest extends AbstractControllerTest {
    private static final String REST_URL_SLASH = REST_URL + '/';

    @Autowired
    private VoteRepository voteRepository;

    @Test
    @WithUserDetails(value = USER3_MAIL)
    void createWithLocation() throws Exception {
        VoteTo newTo = new VoteTo(null, null, RESTAURANT1_ID + 3, null);
        ResultActions action = perform(MockMvcRequestBuilders.post(REST_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(newTo)))
                .andDo(print())
                .andExpect(status().isCreated());

        VoteTo created = VOTE_TO_MATCHER.readFromJson(action);
        int newId = created.id();
        Vote newVote = getNew();
        newVote.setId(newId);
        VoteTo newVoteTo = VoteUtil.createTo(newVote);
        VOTE_TO_MATCHER.assertMatch(created, newVoteTo);
        VOTE_TO_MATCHER.assertMatch(VoteUtil.createTo(voteRepository.getBelonged(USER1_ID + 2, newId)), newVoteTo);
    }

    @Test
    @WithUserDetails(value = USER3_MAIL)
    void createForNotFoundRestaurant() throws Exception {
        VoteTo newTo = new VoteTo(null, null, RestaurantTestData.NOT_FOUND, null);
        perform(MockMvcRequestBuilders.post(REST_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(newTo)))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    @WithUserDetails(value = USER1_MAIL)
    void createDuplicate() throws Exception {
        VoteTo newTo = new VoteTo(null, null, RESTAURANT1_ID, null);
        perform(MockMvcRequestBuilders.post(REST_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(newTo)))
                .andDo(print())
                .andExpect(status().isUnprocessableEntity());
    }

    @Test
    @WithUserDetails(value = USER1_MAIL)
    void updateBeforeDeadline() throws Exception {
        VoteTo updatedTo = new VoteTo(VOTE1_ID, null, RESTAURANT1_ID + 1, null);
        perform(MockMvcRequestBuilders.put(REST_URL_SLASH + VOTE1_ID)
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(updatedTo)))
                .andDo(print())
                .andExpect(status().isNoContent());
        VOTE_TO_MATCHER.assertMatch(VoteUtil.createTo(voteRepository.getExisted(VOTE1_ID)), getUpdated());
    }

    @Test
    @WithUserDetails(value = USER1_MAIL)
    void updateVoteTheSameRestaurant() throws Exception {
        VoteTo updatedTo = new VoteTo(VOTE1_ID, null, RESTAURANT1_ID, null);
        perform(MockMvcRequestBuilders.put(REST_URL_SLASH + VOTE1_ID)
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(updatedTo)))
                .andDo(print())
                .andExpect(status().isUnprocessableEntity());
    }

    @Test
    @WithUserDetails(value = USER3_MAIL)
    void updateNotExistedToday() throws Exception {
        VoteTo updatedTo = new VoteTo(NOT_FOUND, null, RESTAURANT1_ID, null);
        perform(MockMvcRequestBuilders.put(REST_URL_SLASH + NOT_FOUND)
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(updatedTo)))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    @WithUserDetails(value = USER1_MAIL)
    void updateOld() throws Exception {
        VoteTo updatedTo = new VoteTo(VOTE1_ID + 2, null, RESTAURANT1_ID, null);
        perform(MockMvcRequestBuilders.put(REST_URL_SLASH + newYearVote1.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(updatedTo)))
                .andDo(print())
                .andExpect(status().isUnprocessableEntity());
    }

    @Test
    @WithUserDetails(value = USER1_MAIL)
    void get() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL_SLASH + newYearVote1.getId()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(VOTE_TO_MATCHER.contentJson(VoteUtil.createTo(newYearVote1)));
    }

    @Test
    @WithUserDetails(value = USER1_MAIL)
    void getToday() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL_SLASH + "today"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(VOTE_TO_MATCHER.contentJson(VoteUtil.createTo(vote1)));
    }

    @Test
    @WithUserDetails(value = USER1_MAIL)
    void getAll() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(VOTE_TO_MATCHER.contentJson(VoteUtil.getTos(votesForUser1)));
    }
}
