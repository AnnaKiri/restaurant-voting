package ru.annakirillova.restaurantvoting.to;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import ru.annakirillova.restaurantvoting.HasId;

@Data
public abstract class BaseTo implements HasId {

    @Schema(accessMode = Schema.AccessMode.READ_ONLY)
    protected Integer id;

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
