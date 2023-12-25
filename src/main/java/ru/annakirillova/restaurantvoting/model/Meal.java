package ru.annakirillova.restaurantvoting.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.hibernate.validator.constraints.Range;

import java.time.LocalDate;

@Entity
@Table(name = "meal",
        uniqueConstraints = @UniqueConstraint(columnNames = {"restaurant_id", "description", "date"},
                name = "uk_restaurant_description_date"))
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString(callSuper = true, exclude = {"restaurant"})
public class Meal extends AbstractBaseEntity {

    @Column(name = "date", nullable = false, columnDefinition = "date default current_date")
    @NotNull
    private LocalDate date;

    @Column(name = "description", nullable = false)
    @NotBlank
    @Size(min = 2, max = 128)
    private String description;

    @Column(name = "price", nullable = false)
    @Range(min = 0, max = 10000)
    private int price;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "restaurant_id", referencedColumnName = "id", nullable = false)
    private Restaurant restaurant;

    public Meal(LocalDate date, String description, int price, Restaurant restaurant) {
        this(null, date, description, price, restaurant);
    }

    public Meal(Integer id, LocalDate date, String description, int price, Restaurant restaurant) {
        super(id);
        this.date = date;
        this.description = description;
        this.price = price;
        this.restaurant = restaurant;
    }
}
