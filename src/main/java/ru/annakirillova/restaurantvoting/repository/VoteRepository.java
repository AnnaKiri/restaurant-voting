package ru.annakirillova.restaurantvoting.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;
import ru.annakirillova.restaurantvoting.error.DataConflictException;
import ru.annakirillova.restaurantvoting.error.NotFoundException;
import ru.annakirillova.restaurantvoting.model.Vote;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Transactional(readOnly = true)
public interface VoteRepository extends BaseRepository<Vote> {
    @Query("SELECT v FROM Vote v WHERE v.user.id=:userId AND v.created= :date")
    Optional<Vote> getVoteByDate(@Param("userId") int userId, @Param("date") LocalDate date);

    default Vote getExistedByDate(int userId, LocalDate date) {
        return getVoteByDate(userId, date).orElseThrow(() -> new NotFoundException("Vote for user with id=" + userId + " on date " + date + " not found"));
    }

    @Query("SELECT v FROM Vote v WHERE v.user.id= :userId ORDER BY v.created DESC")
    List<Vote> getAllByUser(@Param("userId") int userId);

    @Query("SELECT v FROM Vote v WHERE v.id = :voteId and v.user.id = :userId")
    Optional<Vote> get(int userId, int voteId);

    default Vote getBelonged(int userId, int voteId) {
        return get(userId, voteId).orElseThrow(
                () -> new DataConflictException("Vote id=" + voteId + " is not exist or doesn't belong to User id=" + userId));
    }
}
