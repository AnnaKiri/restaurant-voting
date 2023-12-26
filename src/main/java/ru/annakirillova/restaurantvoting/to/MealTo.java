package ru.annakirillova.restaurantvoting.to;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.validator.constraints.Range;

import java.time.LocalDate;

@Getter
@Setter
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class MealTo extends BaseTo {

    private final LocalDate date;

    @NotBlank
    @Size(min = 2, max = 100)
    private final String description;

    @NotNull
    @Range(min = 0, max = 10000)
    private final Integer price;

    public MealTo(Integer id, LocalDate date, String description, int price) {
        super(id);
        this.date = date;
        this.description = description;
        this.price = price;
    }
}
