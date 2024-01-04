package ru.annakirillova.restaurantvoting.web.user;

import ru.annakirillova.restaurantvoting.model.Role;
import ru.annakirillova.restaurantvoting.model.User;
import ru.annakirillova.restaurantvoting.util.JsonUtil;
import ru.annakirillova.restaurantvoting.web.MatcherFactory;

import java.util.Collections;
import java.util.Date;
import java.util.List;

public class UserTestData {

    public static final int USER1_ID = 1;
    public static final int ADMIN_ID = 4;
    public static final String USER1_MAIL = "user1@yandex.ru";
    public static final String USER2_MAIL = "user2@yandex.ru";
    public static final String USER3_MAIL = "user3@yandex.ru";
    public static final String ADMIN_MAIL = "admin@gmail.com";

    public static final MatcherFactory.Matcher<User> USER_MATCHER = MatcherFactory.usingIgnoringFieldsComparator(User.class, "registered", "password", "votes");

    public static final User user1 = new User(USER1_ID, "User1", USER1_MAIL, "{noop}password1", Role.USER);
    public static final User user2 = new User(USER1_ID + 1, "User2", USER2_MAIL, "{noop}password2", Role.USER);
    public static final User user3 = new User(USER1_ID + 2, "User3", USER3_MAIL, "{noop}password3", Role.USER);
    public static final User admin = new User(ADMIN_ID, "Admin", ADMIN_MAIL, "{noop}admin", Role.ADMIN);

    public static User getNew() {
        return new User(null, "NewUser", "newemail@yandex.ru", "newpassword", false, new Date(), Collections.singleton(Role.USER));
    }

    public static User getUpdated() {
        return new User(USER1_ID, "UpdatedUser1", "updatedemail@yandex.ru", "updatedpassword", false, new Date(), List.of(Role.ADMIN));
    }

    public static String jsonWithPassword(User user, String passw) {
        return JsonUtil.writeAdditionProps(user, "password", passw);
    }
}
