package ru.annakirillova.restaurantvoting.to;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDate;

@Getter
@Setter
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class VoteTo extends BaseTo {

    @Schema(accessMode = Schema.AccessMode.READ_ONLY)
    private Integer userId;

    @NotNull
    private Integer restaurantId;

    @Schema(accessMode = Schema.AccessMode.READ_ONLY)
    private LocalDate created;

    public VoteTo() {
        super(null);
    }

    public VoteTo(Integer id, Integer userId, Integer restaurantId, LocalDate created) {
        super(id);
        this.userId = userId;
        this.restaurantId = restaurantId;
        this.created = created;
    }
}
