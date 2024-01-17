package ru.annakirillova.restaurantvoting.web.vote;

import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import ru.annakirillova.restaurantvoting.to.VoteTo;
import ru.annakirillova.restaurantvoting.util.JsonUtil;
import ru.annakirillova.restaurantvoting.web.AbstractControllerTest;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ru.annakirillova.restaurantvoting.web.restaurant.RestaurantTestData.RESTAURANT1_ID;
import static ru.annakirillova.restaurantvoting.web.user.UserTestData.USER1_MAIL;
import static ru.annakirillova.restaurantvoting.web.vote.ProfileVoteController.REST_URL;
import static ru.annakirillova.restaurantvoting.web.vote.VoteTestData.VOTE1_ID;

@ActiveProfiles("timeAfterDeadline")
public class ProfileVoteControllerAfterDeadlineTest extends AbstractControllerTest {
    private static final String REST_URL_SLASH = REST_URL + '/';

    @Test
    @WithUserDetails(value = USER1_MAIL)
    void updateBeforeDeadline() throws Exception {
        VoteTo updatedTo = new VoteTo(VOTE1_ID, null, RESTAURANT1_ID + 1, null);
        perform(MockMvcRequestBuilders.put(REST_URL_SLASH + VOTE1_ID)
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(updatedTo)))
                .andDo(print())
                .andExpect(status().isUnprocessableEntity());
    }
}
