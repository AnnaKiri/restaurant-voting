package ru.annakirillova.restaurantvoting.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Table(name = "vote",
        uniqueConstraints = @UniqueConstraint(columnNames = {"restaurant_id", "user_id", "date"},
                name = "uk_restaurant_user_date"))
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Vote extends AbstractBaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", referencedColumnName = "id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "restaurant_id", referencedColumnName = "id", nullable = false)
    private Restaurant restaurant;

    @Column(name = "date", nullable = false, columnDefinition = "date default current_date", updatable = false)
    @NotNull
    private LocalDate date;

    @Column(name = "time", nullable = false, columnDefinition = "time default current_time")
    @NotNull
    private LocalTime time;

    public Vote(User user, Restaurant restaurant, LocalDate date, LocalTime time) {
        this(null, user, restaurant, date, time);
    }

    public Vote(Integer id, User user, Restaurant restaurant, LocalDate date, LocalTime time) {
        super(id);
        this.user = user;
        this.restaurant = restaurant;
        this.date = date;
        this.time = time;
    }

    @Override
    public String toString() {
        return "Voting{" +
                "id=" + id +
                ", user=" + user +
                ", restaurant=" + restaurant +
                ", date=" + date +
                ", time=" + time +
                '}';
    }
}
