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
        uniqueConstraints = @UniqueConstraint(columnNames = {"created", "restaurant_id", "description"},
                name = "uk_created_restaurant_description"))
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString(callSuper = true)
public class Meal extends AbstractBaseEntity {

    @Column(name = "created", nullable = false, columnDefinition = "date default current_date")
    @NotNull
    private LocalDate created = LocalDate.now();

    @Column(name = "description", nullable = false)
    @NotBlank
    @Size(min = 2, max = 128)
    @NoHtml
    private String description;

    @Column(name = "price", nullable = false)
    @NotNull
    @Range(min = 0, max = 10000)
    private Integer price;

    @ToString.Exclude
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "restaurant_id", referencedColumnName = "id", nullable = false)
    @JsonIgnore
    private Restaurant restaurant;

    public Meal(Meal meal) {
        this(meal.created, meal.description, meal.price);
    }

    public Meal(LocalDate created, String description, Integer price) {
        this(null, created, description, price);
    }

    public Meal(Integer id, LocalDate created, String description, Integer price) {
        super(id);
        this.created = created;
        this.description = description;
        this.price = price;
    }
}
