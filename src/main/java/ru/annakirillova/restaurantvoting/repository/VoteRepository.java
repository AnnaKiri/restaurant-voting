package ru.annakirillova.restaurantvoting.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;
import ru.annakirillova.restaurantvoting.error.DataConflictException;
import ru.annakirillova.restaurantvoting.model.Vote;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Transactional(readOnly = true)
public interface VoteRepository extends JpaRepository<Vote, Integer> {
    @Query("SELECT v FROM Vote v WHERE v.user.id=:userId AND v.created= :date")
    Optional<Vote> getVoteByDate(@Param("userId") int userId, @Param("date") LocalDate date);

    @Query("SELECT v FROM Vote v WHERE v.restaurant.id= :restaurantsId ORDER BY v.created DESC")
    List<Vote> getAllByRestaurant(@Param("restaurantsId") int restaurantsId);

    @Query("SELECT v from Vote v WHERE v.restaurant.id= :restaurantsId AND v.created = :date ORDER BY v.user.id")
    List<Vote> getAllByDate(@Param("restaurantsId") int restaurantsId, @Param("date") LocalDate date);

    @Query("SELECT v from Vote v WHERE v.created >= :startDate AND v.created < :endDate AND v.restaurant.id = :restaurantId ORDER BY v.created DESC")
    List<Vote> getVotesBetweenDates(@Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate, @Param("restaurantId") int restaurantId);

    @Query("SELECT v FROM Vote v WHERE v.id = :voteId and v.restaurant.id = :restaurantId")
    Optional<Vote> get(int restaurantId, int voteId);

    default Vote getBelonged(int restaurantId, int voteId) {
        return get(restaurantId, voteId).orElseThrow(
                () -> new DataConflictException("Vote id=" + voteId + "   is not exist or doesn't belong to Restaurant id=" + restaurantId));
    }
}
