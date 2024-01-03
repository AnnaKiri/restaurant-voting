package ru.annakirillova.restaurantvoting.util;

import ru.annakirillova.restaurantvoting.model.Vote;
import ru.annakirillova.restaurantvoting.to.VoteTo;

import java.util.Collection;
import java.util.List;

public class VoteUtil {

    public static VoteTo createTo(Vote vote) {
        return new VoteTo(vote.getUser().getId(), vote.getRestaurant().getId(), vote.getDate());
    }

    public static List<VoteTo> getTos(Collection<Vote> votes) {
        return votes.stream().map(VoteUtil::createTo).toList();
    }
}
