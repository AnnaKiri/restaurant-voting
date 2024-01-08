package ru.annakirillova.restaurantvoting.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import java.time.*;

import static ru.annakirillova.restaurantvoting.service.VoteService.RE_VOTE_DEADLINE;

@Configuration
public class TestConfig {

    @Profile("timeAfterDeadline")
    @Bean
    public Clock clockAfterDeadline() {
        LocalTime fixedTime = RE_VOTE_DEADLINE.plusHours(1);
        LocalDate today = LocalDate.now();
        return Clock.fixed(LocalDateTime.of(today, fixedTime).toInstant(ZoneOffset.UTC), ZoneOffset.UTC);
    }

    @Profile("timeBeforeDeadline")
    @Bean
    public Clock clockBeforeDeadline() {
        LocalTime fixedTime = RE_VOTE_DEADLINE.minusHours(1);
        LocalDate today = LocalDate.now();
        return Clock.fixed(LocalDateTime.of(today, fixedTime).toInstant(ZoneOffset.UTC), ZoneOffset.UTC);
    }
}
