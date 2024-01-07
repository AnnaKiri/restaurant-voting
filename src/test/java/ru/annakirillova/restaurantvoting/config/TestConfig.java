package ru.annakirillova.restaurantvoting.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import java.time.*;

@Configuration
public class TestConfig {

    @Profile("timeAfter11")
    @Bean
    public Clock clockAfter11() {
        LocalTime fixedTime = LocalTime.of(12, 0);
        LocalDate today = LocalDate.now();
        return Clock.fixed(LocalDateTime.of(today, fixedTime).toInstant(ZoneOffset.UTC), ZoneOffset.UTC);
    }

    @Profile("timeBefore11")
    @Bean
    public Clock clockBefore11() {
        LocalTime fixedTime = LocalTime.of(9, 0);
        LocalDate today = LocalDate.now();
        return Clock.fixed(LocalDateTime.of(today, fixedTime).toInstant(ZoneOffset.UTC), ZoneOffset.UTC);
    }
}
