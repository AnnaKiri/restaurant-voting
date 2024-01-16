package ru.annakirillova.restaurantvoting.to;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import ru.annakirillova.restaurantvoting.model.Dish;
import ru.annakirillova.restaurantvoting.validation.NoHtml;

import java.util.List;

@Getter
@Setter
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true, exclude = {"dishes"})
public class RestaurantTo extends BaseTo {

    @NotBlank
    @Size(min = 2, max = 100)
    @NoHtml
    private final String name;

    private List<Dish> dishes;

    private Integer rating;

    public RestaurantTo() {
        super(null);
        this.name = null;
    }

    public RestaurantTo(Integer id, String name, long rating) {
        super(id);
        this.name = name;
        this.rating = Math.toIntExact(rating);
    }

    public RestaurantTo(Integer id, String name, List<Dish> dishes, Integer rating) {
        super(id);
        this.name = name;
        this.dishes = dishes;
        this.rating = rating;
    }
}
