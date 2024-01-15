package ru.annakirillova.restaurantvoting.web.dish;


import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import ru.annakirillova.restaurantvoting.error.DataConflictException;
import ru.annakirillova.restaurantvoting.model.Dish;
import ru.annakirillova.restaurantvoting.service.DishService;
import ru.annakirillova.restaurantvoting.util.JsonUtil;
import ru.annakirillova.restaurantvoting.web.AbstractControllerTest;
import ru.annakirillova.restaurantvoting.web.restaurant.RestaurantTestData;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ru.annakirillova.restaurantvoting.web.dish.AdminDishController.REST_URL;
import static ru.annakirillova.restaurantvoting.web.dish.DishTestData.*;
import static ru.annakirillova.restaurantvoting.web.restaurant.RestaurantTestData.RESTAURANT1_ID;
import static ru.annakirillova.restaurantvoting.web.user.UserTestData.ADMIN_MAIL;

class AdminDishControllerTest extends AbstractControllerTest {

    private static final String REST_URL_SLASH = REST_URL + '/';

    @Autowired
    private DishService dishService;

    @Test
    @WithUserDetails(value = ADMIN_MAIL)
    void get() throws Exception {
        perform(MockMvcRequestBuilders.get(buildUrlWithRestaurantId(REST_URL_SLASH, RESTAURANT1_ID) + DISH1_ID))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(DISH_MATCHER.contentJson(DISH_1));
    }

    @Test
    void getUnAuth() throws Exception {
        perform(MockMvcRequestBuilders.get(buildUrlWithRestaurantId(REST_URL_SLASH, RESTAURANT1_ID) + DISH1_ID))
                .andExpect(status().isUnauthorized());
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
        perform(MockMvcRequestBuilders.get(buildUrlWithRestaurantId(REST_URL_SLASH, RestaurantTestData.NOT_FOUND) + DISH1_ID))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    @WithUserDetails(value = ADMIN_MAIL)
    void delete() throws Exception {
        perform(MockMvcRequestBuilders.delete(buildUrlWithRestaurantId(REST_URL_SLASH, RESTAURANT1_ID) + DISH1_ID))
                .andExpect(status().isNoContent());
        assertThrows(DataConflictException.class, () -> dishService.get(RESTAURANT1_ID, DISH1_ID));
    }

    @Test
    @WithUserDetails(value = ADMIN_MAIL)
    void deleteDataConflict() throws Exception {
        perform(MockMvcRequestBuilders.delete(buildUrlWithRestaurantId(REST_URL_SLASH, RESTAURANT1_ID + 1) + DISH1_ID))
                .andExpect(status().isConflict());
    }

    @Test
    @WithUserDetails(value = ADMIN_MAIL)
    void update() throws Exception {
        Dish updated = getUpdated();
        perform(MockMvcRequestBuilders.put(buildUrlWithRestaurantId(REST_URL_SLASH, RESTAURANT1_ID) + DISH1_ID)
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(updated)))
                .andExpect(status().isNoContent());
        DISH_MATCHER.assertMatch(dishService.get(RESTAURANT1_ID, updated.getId()), getUpdated());
    }

    @Test
    @WithUserDetails(value = ADMIN_MAIL)
    void updateInvalid() throws Exception {
        Dish invalid = new Dish(DISH1_ID, null, null, 6000);
        perform(MockMvcRequestBuilders.put(buildUrlWithRestaurantId(REST_URL_SLASH, RESTAURANT1_ID) + DISH1_ID)
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(invalid)))
                .andDo(print())
                .andExpect(status().isUnprocessableEntity());
    }

    @Test
    @Transactional(propagation = Propagation.NEVER)
    @WithUserDetails(value = ADMIN_MAIL)
    void updateDuplicate() throws Exception {
        Dish invalid = new Dish(DISH1_ID, DISH_2.getAvailableOn(), DISH_2.getDescription(), 200);
        perform(MockMvcRequestBuilders.put(buildUrlWithRestaurantId(REST_URL_SLASH, RESTAURANT1_ID) + DISH1_ID)
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(invalid)))
                .andDo(print())
                .andExpect(status().isConflict());
    }

    @Test
    @WithUserDetails(value = ADMIN_MAIL)
    void createWithLocation() throws Exception {
        Dish newDish = getNew();
        ResultActions action = perform(MockMvcRequestBuilders.post(buildUrlWithRestaurantId(REST_URL, RESTAURANT1_ID))
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(newDish)));

        Dish created = DISH_MATCHER.readFromJson(action);
        int newId = created.id();
        newDish.setId(newId);
        DISH_MATCHER.assertMatch(created, newDish);
        DISH_MATCHER.assertMatch(dishService.get(RESTAURANT1_ID, newId), newDish);
    }

    @Test
    @WithUserDetails(value = ADMIN_MAIL)
    void createForNotFound() throws Exception {
        Dish invalid = getNew();
        perform(MockMvcRequestBuilders.post(buildUrlWithRestaurantId(REST_URL, NOT_FOUND))
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(invalid)))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    @WithUserDetails(value = ADMIN_MAIL)
    void createInvalid() throws Exception {
        Dish invalid = new Dish(null, null, "Dummy", 200);
        perform(MockMvcRequestBuilders.post(buildUrlWithRestaurantId(REST_URL, RESTAURANT1_ID))
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(invalid)))
                .andDo(print())
                .andExpect(status().isUnprocessableEntity());
    }

    @Test
    @WithUserDetails(value = ADMIN_MAIL)
    @DirtiesContext
    void createDuplicate() throws Exception {
        Dish invalid = new Dish(null, LocalDate.now(), DISH_1.getDescription(), 200);
        perform(MockMvcRequestBuilders.post(buildUrlWithRestaurantId(REST_URL, RESTAURANT1_ID))
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(invalid)))
                .andDo(print())
                .andExpect(status().isConflict());
    }

    @Test
    @WithUserDetails(value = ADMIN_MAIL)
    void getAll() throws Exception {
        perform(MockMvcRequestBuilders.get(buildUrlWithRestaurantId(REST_URL, RESTAURANT1_ID)))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(DISH_MATCHER.contentJson(DISHES_FOR_DICKINSON));
    }

    @Test
    @WithUserDetails(value = ADMIN_MAIL)
    void getBetween() throws Exception {
        perform(MockMvcRequestBuilders.get(buildUrlWithRestaurantId(REST_URL_SLASH, RESTAURANT1_ID) + "filter")
                .param("startDate", "2024-01-01")
                .param("endDate", "2024-01-03"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(DISH_MATCHER.contentJson(List.of(NEW_YEAR_DISH_1)));
    }

    @Test
    @WithUserDetails(value = ADMIN_MAIL)
    void getAllToday() throws Exception {
        perform(MockMvcRequestBuilders.get(buildUrlWithRestaurantId(REST_URL_SLASH, RESTAURANT1_ID) + "today"))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(DISH_MATCHER.contentJson(DISHES_FOR_DICKINSON_TODAY));
    }

    @Test
    @WithUserDetails(value = ADMIN_MAIL)
    void updateHtmlUnsafe() throws Exception {
        Dish updated = getUpdated();
        updated.setDescription("<script>alert(123)</script>");
        perform(MockMvcRequestBuilders.put(buildUrlWithRestaurantId(REST_URL_SLASH, RESTAURANT1_ID) + DISH1_ID)
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(updated)))
                .andDo(print())
                .andExpect(status().isUnprocessableEntity());
    }
}