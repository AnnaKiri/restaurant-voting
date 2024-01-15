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
        uniqueConstraints = @UniqueConstraint(columnNames = {"available_on", "restaurant_id", "description"},
                name = "uk_created_restaurant_description"))
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString(callSuper = true)
public class Meal extends AbstractBaseEntity {

    @Column(name = "available_on", nullable = false, columnDefinition = "date default current_date")
    @NotNull
    private LocalDate availableOn = LocalDate.now();

    @Column(name = "description", nullable = false)
    @NotBlank
    @Size(min = 2, max = 128)
    @NoHtml
    private String description;

    @Column(name = "price", nullable = false)
    @NotNull
    @Range(min = 0, max = 10_000_000)
    private Integer price;

    @ToString.Exclude
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "restaurant_id", referencedColumnName = "id", nullable = false)
    @JsonIgnore
    private Restaurant restaurant;

    public Meal(Meal meal) {
        this(meal.availableOn, meal.description, meal.price);
    }

    public Meal(LocalDate availableOn, String description, Integer price) {
        this(null, availableOn, description, price);
    }

    public Meal(Integer id, LocalDate availableOn, String description, Integer price) {
        super(id);
        this.availableOn = availableOn;
        this.description = description;
        this.price = price;
    }
}
