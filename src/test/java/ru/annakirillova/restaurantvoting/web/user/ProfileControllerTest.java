package ru.annakirillova.restaurantvoting.web.user;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import ru.annakirillova.restaurantvoting.model.User;
import ru.annakirillova.restaurantvoting.repository.UserRepository;
import ru.annakirillova.restaurantvoting.to.UserTo;
import ru.annakirillova.restaurantvoting.util.JsonUtil;
import ru.annakirillova.restaurantvoting.util.UsersUtil;
import ru.annakirillova.restaurantvoting.web.AbstractControllerTest;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ru.annakirillova.restaurantvoting.web.user.ProfileController.REST_URL;
import static ru.annakirillova.restaurantvoting.web.user.UserTestData.ADMIN_MAIL;
import static ru.annakirillova.restaurantvoting.web.user.UserTestData.USER1_ID;
import static ru.annakirillova.restaurantvoting.web.user.UserTestData.USER1_MAIL;
import static ru.annakirillova.restaurantvoting.web.user.UserTestData.USER_MATCHER;
import static ru.annakirillova.restaurantvoting.web.user.UserTestData.admin;
import static ru.annakirillova.restaurantvoting.web.user.UserTestData.user1;
import static ru.annakirillova.restaurantvoting.web.user.UserTestData.user2;
import static ru.annakirillova.restaurantvoting.web.user.UserTestData.user3;

class ProfileControllerTest extends AbstractControllerTest {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private AdminUserController adminUserController;

    @Test
    @WithUserDetails(value = USER1_MAIL)
    void get() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(USER_MATCHER.contentJson(user1));
    }

    @Test
    @WithUserDetails(value = USER1_MAIL)
    void delete() throws Exception {
        perform(MockMvcRequestBuilders.delete(REST_URL))
                .andExpect(status().isNoContent());
        USER_MATCHER.assertMatch(adminUserController.getAll(), admin, user2, user3);
    }

    @Test
    void register() throws Exception {
        UserTo newTo = new UserTo(null, "NewUser", "newemail@yandex.ru", "newpassword");
        User newUser = UsersUtil.createNewFromTo(newTo);
        ResultActions action = perform(MockMvcRequestBuilders.post(REST_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(newTo)))
                .andDo(print())
                .andExpect(status().isCreated());

        User created = USER_MATCHER.readFromJson(action);
        int newId = created.id();
        newUser.setId(newId);
        USER_MATCHER.assertMatch(created, newUser);
        USER_MATCHER.assertMatch(userRepository.getExisted(newId), newUser);
    }

    @Test
    @WithUserDetails(value = USER1_MAIL)
    void update() throws Exception {
        UserTo updatedTo = new UserTo(null, "UpdatedUser1", "updatedemail@yandex.ru", "updatedpassword");
        perform(MockMvcRequestBuilders.put(REST_URL).contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(updatedTo)))
                .andDo(print())
                .andExpect(status().isNoContent());

        USER_MATCHER.assertMatch(userRepository.getExisted(USER1_ID), UsersUtil.updateFromTo(new User(user1), updatedTo));
    }

    @Test
    @WithUserDetails(value = USER1_MAIL)
    void updateDuplicate() throws Exception {
        UserTo updatedTo = new UserTo(null, "newName", ADMIN_MAIL, "newPassword");
        perform(MockMvcRequestBuilders.put(REST_URL).contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(updatedTo)))
                .andDo(print())
                .andExpect(status().isUnprocessableEntity())
                .andExpect(content().string(containsString(UniqueMailValidator.EXCEPTION_DUPLICATE_EMAIL)));
    }
}