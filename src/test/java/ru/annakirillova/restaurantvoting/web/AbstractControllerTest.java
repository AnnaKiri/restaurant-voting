package ru.annakirillova.restaurantvoting.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.transaction.annotation.Transactional;
import ru.annakirillova.restaurantvoting.config.DateTimeProvider;

@SpringBootTest
@Transactional
@AutoConfigureMockMvc
@ActiveProfiles("test")
public abstract class AbstractControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    protected DateTimeProvider dateTimeProvider;

    protected ResultActions perform(MockHttpServletRequestBuilder builder) throws Exception {
        return mockMvc.perform(builder);
    }

    protected String buildUrlWithRestaurantId(String url, int restaurantId) {
        return url.replace("{restaurantId}", Integer.toString(restaurantId));
    }
}
