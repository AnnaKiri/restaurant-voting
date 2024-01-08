package ru.annakirillova.restaurantvoting.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@Table(name = "vote",
        uniqueConstraints = @UniqueConstraint(columnNames = {"user_id", "created", "restaurant_id"},
                name = "uk_user_created_restaurant"))
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

    @Column(name = "created", nullable = false, columnDefinition = "date default current_date", updatable = false)
    @NotNull
    private LocalDate created = LocalDate.now();

    public Vote(User user, Restaurant restaurant) {
        this.user = user;
        this.restaurant = restaurant;
    }

    public Vote(Integer id, User user, Restaurant restaurant, LocalDate created) {
        super(id);
        this.user = user;
        this.restaurant = restaurant;
        this.created = created;
    }

    @Override
    public String toString() {
        return "Voting{" +
                "id=" + id +
                ", user_id=" + user.getId() +
                ", restaurant_id=" + restaurant.getId() +
                ", date=" + created +
                '}';
    }
}
