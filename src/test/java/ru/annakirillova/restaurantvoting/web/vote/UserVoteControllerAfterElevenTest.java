package ru.annakirillova.restaurantvoting.web.vote;

import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import ru.annakirillova.restaurantvoting.web.AbstractControllerTest;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ru.annakirillova.restaurantvoting.web.restaurant.RestaurantTestData.RESTAURANT1_ID;
import static ru.annakirillova.restaurantvoting.web.user.UserTestData.USER1_MAIL;

@ActiveProfiles("timeAfter11")
public class UserVoteControllerAfterElevenTest extends AbstractControllerTest {

    @Test
    @WithUserDetails(value = USER1_MAIL)
    void updateAfterEleven() throws Exception {
        perform(MockMvcRequestBuilders.post(buildUrlWithRestaurantId(UserVoteController.REST_URL, RESTAURANT1_ID + 1))
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isUnprocessableEntity());
    }
}
