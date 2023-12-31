package ru.annakirillova.restaurantvoting.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.util.List;

@Entity
@Table(name = "restaurant")
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString(callSuper = true, exclude = {"meals", "votes"})
public class Restaurant extends AbstractNamedEntity {

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "restaurant")
    @OrderBy("description ASC")
    @OnDelete(action = OnDeleteAction.CASCADE)
    @Schema(accessMode = Schema.AccessMode.READ_ONLY)
    private List<Meal> meals;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "restaurant")
    @OnDelete(action = OnDeleteAction.CASCADE)
    @Schema(accessMode = Schema.AccessMode.READ_ONLY)
    private List<Vote> votes;

    public Restaurant(Restaurant r) {
        super(r.id, r.name);
    }

    public Restaurant(Integer id, String name) {
        super(id, name);
    }
}
