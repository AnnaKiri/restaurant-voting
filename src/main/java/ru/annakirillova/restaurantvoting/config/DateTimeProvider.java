package ru.annakirillova.restaurantvoting.config;

import lombok.AllArgsConstructor;

import java.time.Clock;
import java.time.LocalDate;
import java.time.LocalTime;

@AllArgsConstructor
public class DateTimeProvider {
    private final Clock clock;

    public LocalDate getNowDate() {
        return LocalDate.now(clock);
    }

    public LocalTime getNowTime() {
        return LocalTime.now(clock);
    }
}
