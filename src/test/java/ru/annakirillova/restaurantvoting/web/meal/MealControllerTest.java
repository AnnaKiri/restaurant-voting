package ru.annakirillova.restaurantvoting.web.meal;


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
import ru.annakirillova.restaurantvoting.model.Meal;
import ru.annakirillova.restaurantvoting.service.MealService;
import ru.annakirillova.restaurantvoting.util.JsonUtil;
import ru.annakirillova.restaurantvoting.web.AbstractControllerTest;
import ru.annakirillova.restaurantvoting.web.restaurant.RestaurantTestData;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ru.annakirillova.restaurantvoting.web.meal.AdminMealController.REST_URL;
import static ru.annakirillova.restaurantvoting.web.meal.MealTestData.*;
import static ru.annakirillova.restaurantvoting.web.restaurant.RestaurantTestData.RESTAURANT1_ID;
import static ru.annakirillova.restaurantvoting.web.user.UserTestData.ADMIN_MAIL;

class MealControllerTest extends AbstractControllerTest {

    private static final String REST_URL_SLASH = REST_URL + '/';

    @Autowired
    private MealService mealService;

    @Test
    @WithUserDetails(value = ADMIN_MAIL)
    void get() throws Exception {
        perform(MockMvcRequestBuilders.get(buildUrlWithRestaurantId(REST_URL_SLASH, RESTAURANT1_ID) + MEAL1_ID))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(MEAL_MATCHER.contentJson(meal1));
    }

    @Test
    void getUnAuth() throws Exception {
        perform(MockMvcRequestBuilders.get(buildUrlWithRestaurantId(REST_URL_SLASH, RESTAURANT1_ID) + MEAL1_ID))
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
        perform(MockMvcRequestBuilders.get(buildUrlWithRestaurantId(REST_URL_SLASH, RestaurantTestData.NOT_FOUND) + MEAL1_ID))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    @WithUserDetails(value = ADMIN_MAIL)
    void delete() throws Exception {
        perform(MockMvcRequestBuilders.delete(buildUrlWithRestaurantId(REST_URL_SLASH, RESTAURANT1_ID) + MEAL1_ID))
                .andExpect(status().isNoContent());
        assertThrows(DataConflictException.class, () -> mealService.get(RESTAURANT1_ID, MEAL1_ID));
    }

    @Test
    @WithUserDetails(value = ADMIN_MAIL)
    void deleteDataConflict() throws Exception {
        perform(MockMvcRequestBuilders.delete(buildUrlWithRestaurantId(REST_URL_SLASH, RESTAURANT1_ID + 1) + MEAL1_ID))
                .andExpect(status().isConflict());
    }

    @Test
    @WithUserDetails(value = ADMIN_MAIL)
    void update() throws Exception {
        Meal updated = getUpdated();
        perform(MockMvcRequestBuilders.put(buildUrlWithRestaurantId(REST_URL_SLASH, RESTAURANT1_ID) + MEAL1_ID)
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(updated)))
                .andExpect(status().isNoContent());
        MEAL_MATCHER.assertMatch(mealService.get(RESTAURANT1_ID, updated.getId()), getUpdated());
    }

    @Test
    @WithUserDetails(value = ADMIN_MAIL)
    void updateInvalid() throws Exception {
        Meal invalid = new Meal(MEAL1_ID, null, null, 6000);
        perform(MockMvcRequestBuilders.put(buildUrlWithRestaurantId(REST_URL_SLASH, RESTAURANT1_ID) + MEAL1_ID)
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(invalid)))
                .andDo(print())
                .andExpect(status().isUnprocessableEntity());
    }

    @Test
    @Transactional(propagation = Propagation.NEVER)
    @WithUserDetails(value = ADMIN_MAIL)
    void updateDuplicate() throws Exception {
        Meal invalid = new Meal(MEAL1_ID, meal2.getAvailableOn(), meal2.getDescription(), 200);
        perform(MockMvcRequestBuilders.put(buildUrlWithRestaurantId(REST_URL_SLASH, RESTAURANT1_ID) + MEAL1_ID)
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(invalid)))
                .andDo(print())
                .andExpect(status().isConflict());
    }

    @Test
    @WithUserDetails(value = ADMIN_MAIL)
    void createWithLocation() throws Exception {
        Meal newMeal = getNew();
        ResultActions action = perform(MockMvcRequestBuilders.post(buildUrlWithRestaurantId(REST_URL, RESTAURANT1_ID))
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(newMeal)));

        Meal created = MEAL_MATCHER.readFromJson(action);
        int newId = created.id();
        newMeal.setId(newId);
        MEAL_MATCHER.assertMatch(created, newMeal);
        MEAL_MATCHER.assertMatch(mealService.get(RESTAURANT1_ID, newId), newMeal);
    }

    @Test
    @WithUserDetails(value = ADMIN_MAIL)
    void createForNotFound() throws Exception {
        Meal invalid = getNew();
        perform(MockMvcRequestBuilders.post(buildUrlWithRestaurantId(REST_URL, NOT_FOUND))
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(invalid)))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    @WithUserDetails(value = ADMIN_MAIL)
    void createInvalid() throws Exception {
        Meal invalid = new Meal(null, null, "Dummy", 200);
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
        Meal invalid = new Meal(null, LocalDate.now(), meal1.getDescription(), 200);
        perform(MockMvcRequestBuilders.post(buildUrlWithRestaurantId(REST_URL, RESTAURANT1_ID))
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(invalid)))
                .andDo(print())
                .andExpect(status().isConflict());
    }

    @Test
    @WithUserDetails(value = ADMIN_MAIL)
    void createMealList() throws Exception {
        List<Meal> newListOfMeals = List.of(new Meal(newMeal1), new Meal(newMeal2), new Meal(newMeal3));
        perform(MockMvcRequestBuilders.post(buildUrlWithRestaurantId(REST_URL_SLASH, RESTAURANT1_ID) + "add-list")
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(newListOfMeals)))
                .andExpect(MEAL_MATCHER.contentJson(newMeals));
    }

    @Test
    @WithUserDetails(value = ADMIN_MAIL)
    void createMealListForNotFound() throws Exception {
        List<Meal> newListOfMeals = List.of(new Meal(newMeal1), new Meal(newMeal2), new Meal(newMeal3));
        perform(MockMvcRequestBuilders.post(buildUrlWithRestaurantId(REST_URL_SLASH, RestaurantTestData.NOT_FOUND) + "add-list")
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(newListOfMeals)))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithUserDetails(value = ADMIN_MAIL)
    void getAll() throws Exception {
        perform(MockMvcRequestBuilders.get(buildUrlWithRestaurantId(REST_URL, RESTAURANT1_ID)))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(MEAL_MATCHER.contentJson(mealsForDickinson));
    }

    @Test
    @WithUserDetails(value = ADMIN_MAIL)
    void getBetween() throws Exception {
        perform(MockMvcRequestBuilders.get(buildUrlWithRestaurantId(REST_URL_SLASH, RESTAURANT1_ID) + "filter")
                .param("startDate", "2024-01-01")
                .param("endDate", "2024-01-03"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(MEAL_MATCHER.contentJson(List.of(newYearMeal1)));
    }

    @Test
    @WithUserDetails(value = ADMIN_MAIL)
    void getAllToday() throws Exception {
        perform(MockMvcRequestBuilders.get(buildUrlWithRestaurantId(REST_URL_SLASH, RESTAURANT1_ID) + "today"))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(MEAL_MATCHER.contentJson(mealsForDickinsonToday));
    }

    @Test
    @WithUserDetails(value = ADMIN_MAIL)
    void updateHtmlUnsafe() throws Exception {
        Meal updated = getUpdated();
        updated.setDescription("<script>alert(123)</script>");
        perform(MockMvcRequestBuilders.put(buildUrlWithRestaurantId(REST_URL_SLASH, RESTAURANT1_ID) + MEAL1_ID)
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(updated)))
                .andDo(print())
                .andExpect(status().isUnprocessableEntity());
    }
}