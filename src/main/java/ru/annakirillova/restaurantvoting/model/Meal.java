package ru.annakirillova.restaurantvoting.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.hibernate.validator.constraints.Range;
import ru.annakirillova.restaurantvoting.validation.NoHtml;

import java.time.LocalDate;

@Entity
@Table(name = "meal",
        uniqueConstraints = @UniqueConstraint(columnNames = {"date", "restaurant_id", "description"},
                name = "uk_date_restaurant_description"))
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString(callSuper = true, exclude = {"restaurant"})
public class Meal extends AbstractBaseEntity {

    @Column(name = "date", nullable = false, columnDefinition = "date default current_date")
    @NotNull
    private LocalDate date = LocalDate.now();

    @Column(name = "description", nullable = false)
    @NotBlank
    @Size(min = 2, max = 128)
    @NoHtml
    private String description;

    @Column(name = "price", nullable = false)
    @NotNull
    @Range(min = 0, max = 10000)
    private Integer price;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "restaurant_id", referencedColumnName = "id", nullable = false)
    @JsonIgnore
    private Restaurant restaurant;

    public Meal(Meal meal) {
        this(meal.date, meal.description, meal.price, meal.restaurant);
    }

    public Meal(LocalDate date, String description, Integer price, Restaurant restaurant) {
        this(null, date, description, price, restaurant);
    }

    public Meal(Integer id, LocalDate date, String description, Integer price, Restaurant restaurant) {
        super(id);
        this.date = date;
        this.description = description;
        this.price = price;
        this.restaurant = restaurant;
    }
}
