package ru.annakirillova.restaurantvoting.to;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import ru.annakirillova.restaurantvoting.model.User;

import java.time.LocalDate;
import java.time.LocalTime;

@Getter
@Setter
@EqualsAndHashCode(callSuper = true)
public class VoteTo extends BaseTo {

    private final LocalDate date;

    private final LocalTime time;

    private final User user;

    public VoteTo(LocalDate date, LocalTime time, User user) {
        this.date = date;
        this.time = time;
        this.user = user;
    }

    @Override
    public String toString() {
        return "VoteTo{" +
                "date=" + date +
                ", time=" + time +
                ", user_id=" + user.getId() +
                ", id=" + id +
                '}';
    }
}
