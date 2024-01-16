package ru.annakirillova.restaurantvoting.to;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import ru.annakirillova.restaurantvoting.model.Dish;

import java.util.List;

@Getter
@Setter
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class RestaurantTo extends BaseTo {

    private String name;

    @ToString.Exclude
    private List<Dish> dishes;

    private Integer rating;

    public RestaurantTo() {
        super(null);
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
