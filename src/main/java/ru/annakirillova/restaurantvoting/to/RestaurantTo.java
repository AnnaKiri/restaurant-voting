package ru.annakirillova.restaurantvoting.to;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import ru.annakirillova.restaurantvoting.model.Meal;

import java.util.List;

@Getter
@Setter
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true, exclude = {"meals"})
public class RestaurantTo extends BaseTo {

    @NotBlank
    @Size(min = 2, max = 100)
    private final String name;

    private final List<Meal> meals;

    private final Integer rating;

    public RestaurantTo(String name, List<Meal> meals, Integer rating) {
        this.name = name;
        this.meals = meals;
        this.rating = rating;
    }
}
