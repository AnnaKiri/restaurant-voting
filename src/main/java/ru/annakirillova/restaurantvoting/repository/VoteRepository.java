package ru.annakirillova.restaurantvoting.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;
import ru.annakirillova.restaurantvoting.model.Vote;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Transactional(readOnly = true)
public interface VoteRepository extends JpaRepository<Vote, Integer> {
    @Modifying
    @Transactional
    @Query("DELETE FROM Vote v WHERE v.id= :id")
    int delete(@Param("id") int id);

    @Query("SELECT v FROM Vote v WHERE v.user.id=:userId AND v.date= :date ORDER BY v.date DESC")
    Optional<Vote> getVoteByDate(@Param("userId") int userId, @Param("date") LocalDate date);

    @Query("SELECT v FROM Vote v WHERE v.restaurant.id= :restaurantsId ORDER BY v.date DESC")
    List<Vote> getAllByRestaurant(@Param("restaurantsId") int restaurantsId);

    @Query("SELECT v from Vote v WHERE v.restaurant.id= :restaurantsId AND v.date = :date ORDER BY v.user.id")
    List<Vote> getAllByDate(@Param("restaurantsId") int restaurantsId, @Param("date") LocalDate date);

    @Query("SELECT v from Vote v WHERE v.date >= :startDate AND v.date < :endDate AND v.restaurant.id = :restaurantId ORDER BY v.date DESC")
    List<Vote> getBetweenHalfOpen(@Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate, @Param("restaurantId") int restaurantId);
}
