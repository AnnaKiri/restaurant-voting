package ru.annakirillova.restaurantvoting.web.restaurant;

import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import ru.annakirillova.restaurantvoting.util.RestaurantUtil;
import ru.annakirillova.restaurantvoting.web.AbstractControllerTest;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ru.annakirillova.restaurantvoting.web.restaurant.RestaurantTestData.*;
import static ru.annakirillova.restaurantvoting.web.restaurant.UserRestaurantController.REST_URL;
import static ru.annakirillova.restaurantvoting.web.user.UserTestData.USER1_MAIL;

public class UserRestaurantControllerTest extends AbstractControllerTest {
    private static final String REST_URL_SLASH = REST_URL + '/';

    @Test
    @WithUserDetails(value = USER1_MAIL)
    void getAllWithRating() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL)
                .param("ratingToday", "true"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(RESTAURANT_TO_WITH_VOTES_MATCHER.contentJson(RestaurantUtil.getTos(restaurants)));
    }

    @Test
    @WithUserDetails(value = USER1_MAIL)
    void getAllWithDishes() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL)
                .param("dishesToday", "true"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(RESTAURANT_TO_WITH_DISHES_MATCHER.contentJson(RestaurantUtil.getTos(restaurants)));
    }

    @Test
    @WithUserDetails(value = USER1_MAIL)
    void getAllWithDishesAndRating() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL)
                .param("ratingToday", "true")
                .param("dishesToday", "true"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(RESTAURANT_TO_WITH_VOTES_MATCHER.contentJson(RestaurantUtil.getTos(restaurants)))
                .andExpect(RESTAURANT_TO_WITH_DISHES_MATCHER.contentJson(RestaurantUtil.getTos(restaurants)));
    }

    @Test
    @WithUserDetails(value = USER1_MAIL)
    void getAll() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(RESTAURANT_TO_MATCHER.contentJson(RestaurantUtil.getTos(restaurants)));
    }

    @Test
    @WithUserDetails(value = USER1_MAIL)
    void getWithDishesAndRating() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL_SLASH + RESTAURANT1_ID + "/with-dishes-and-rating"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(RESTAURANT_TO_WITH_DISHES_MATCHER.contentJson(RestaurantUtil.createTo(dickinson)))
                .andExpect(RESTAURANT_TO_WITH_VOTES_MATCHER.contentJson(RestaurantUtil.createTo(dickinson)));
    }
}
