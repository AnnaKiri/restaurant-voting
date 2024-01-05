package ru.annakirillova.restaurantvoting.to;

import lombok.Data;
import ru.annakirillova.restaurantvoting.HasId;

@Data
public abstract class BaseTo implements HasId {
    protected Integer id;

    public BaseTo() {
    }

    public BaseTo(Integer id) {
        this.id = id;
    }

    @Override
    public Integer getId() {
        return id;
    }

    @Override
    public void setId(Integer id) {
        this.id = id;
    }
}
