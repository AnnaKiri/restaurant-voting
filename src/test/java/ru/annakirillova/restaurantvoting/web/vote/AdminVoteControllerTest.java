package ru.annakirillova.restaurantvoting.web.vote;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import ru.annakirillova.restaurantvoting.error.DataConflictException;
import ru.annakirillova.restaurantvoting.service.VoteService;
import ru.annakirillova.restaurantvoting.util.VoteUtil;
import ru.annakirillova.restaurantvoting.web.AbstractControllerTest;
import ru.annakirillova.restaurantvoting.web.restaurant.RestaurantTestData;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ru.annakirillova.restaurantvoting.web.restaurant.RestaurantTestData.RESTAURANT1_ID;
import static ru.annakirillova.restaurantvoting.web.user.UserTestData.ADMIN_MAIL;
import static ru.annakirillova.restaurantvoting.web.vote.AdminVoteController.REST_URL;
import static ru.annakirillova.restaurantvoting.web.vote.VoteTestData.*;

public class AdminVoteControllerTest extends AbstractControllerTest {

    private static final String REST_URL_SLASH = REST_URL + '/';

    @Autowired
    private VoteService voteService;

    @Test
    @WithUserDetails(value = ADMIN_MAIL)
    void get() throws Exception {
        perform(MockMvcRequestBuilders.get(buildUrlWithRestaurantId(REST_URL_SLASH, RESTAURANT1_ID) + VOTE1_ID))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(VOTE_TO_MATCHER.contentJson(VoteUtil.createTo(vote1)));
    }

    @Test
    @WithUserDetails(value = ADMIN_MAIL)
    void getNotFound() throws Exception {
        perform(MockMvcRequestBuilders.get(buildUrlWithRestaurantId(REST_URL_SLASH, RESTAURANT1_ID) + NOT_FOUND))
                .andDo(print())
                .andExpect(status().isConflict());
    }

    @Test
    @WithUserDetails(value = ADMIN_MAIL)
    void getForNotFoundRestaurant() throws Exception {
        perform(MockMvcRequestBuilders.get(buildUrlWithRestaurantId(REST_URL_SLASH, RestaurantTestData.NOT_FOUND) + VOTE1_ID))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    @WithUserDetails(value = ADMIN_MAIL)
    void delete() throws Exception {
        perform(MockMvcRequestBuilders.delete(buildUrlWithRestaurantId(REST_URL_SLASH, RESTAURANT1_ID) + VOTE1_ID))
                .andExpect(status().isNoContent());
        assertThrows(DataConflictException.class, () -> voteService.get(RESTAURANT1_ID, VOTE1_ID));

    }

    @Test
    @WithUserDetails(value = ADMIN_MAIL)
    void deleteNotFound() throws Exception {
        perform(MockMvcRequestBuilders.delete(buildUrlWithRestaurantId(REST_URL_SLASH, RESTAURANT1_ID) + NOT_FOUND))
                .andDo(print())
                .andExpect(status().isConflict());
    }

    @Test
    @WithUserDetails(value = ADMIN_MAIL)
    void deleteForNotFoundRestaurant() throws Exception {
        perform(MockMvcRequestBuilders.delete(buildUrlWithRestaurantId(REST_URL_SLASH, RestaurantTestData.NOT_FOUND) + VOTE1_ID))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    @WithUserDetails(value = ADMIN_MAIL)
    void deleteDataConflict() throws Exception {
        perform(MockMvcRequestBuilders.delete(buildUrlWithRestaurantId(REST_URL_SLASH, RESTAURANT1_ID + 1) + VOTE1_ID))
                .andExpect(status().isConflict());
    }

    @Test
    @WithUserDetails(value = ADMIN_MAIL)
    void getAllByRestaurant() throws Exception {
        perform(MockMvcRequestBuilders.get(buildUrlWithRestaurantId(REST_URL, RESTAURANT1_ID)))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(VOTE_TO_MATCHER.contentJson(VoteUtil.getTos(votesForRestaurant1AllTime)));
    }

    @Test
    @WithUserDetails(value = ADMIN_MAIL)
    void getAllToday() throws Exception {
        perform(MockMvcRequestBuilders.get(buildUrlWithRestaurantId(REST_URL_SLASH, RESTAURANT1_ID) + "today"))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(VOTE_TO_MATCHER.contentJson(VoteUtil.getTos(votesForRestaurant1Today)));
    }

    @Test
    @WithUserDetails(value = ADMIN_MAIL)
    void getBetween() throws Exception {
        perform(MockMvcRequestBuilders.get(buildUrlWithRestaurantId(REST_URL_SLASH, RESTAURANT1_ID) + "filter")
                .param("startDate", "2024-01-01")
                .param("endDate", "2024-01-04"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(VOTE_TO_MATCHER.contentJson(VoteUtil.getTos(List.of(newYearVote4))));
    }
}
