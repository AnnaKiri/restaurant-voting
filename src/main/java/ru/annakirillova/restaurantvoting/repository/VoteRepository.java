package ru.annakirillova.restaurantvoting.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;
import ru.annakirillova.restaurantvoting.model.Vote;

import java.time.LocalDate;
import java.util.List;

@Transactional(readOnly = true)
public interface VoteRepository extends JpaRepository<Vote, Integer> {
    @Modifying
    @Transactional
    @Query("DELETE FROM Vote v WHERE v.id=:id AND v.user.id=:userId")
    int delete(@Param("id") int id, @Param("userId") int userId);

    @Query("SELECT v FROM Vote v WHERE v.restaurant.id=:restaurantsId ORDER BY v.date, v.time DESC")
    List<Vote> getAllByRestaurant(@Param("restaurantsId") int restaurantsId);

    @Query("SELECT v from Vote v WHERE v.restaurant.id=:restaurantsId AND v.date = :date ORDER BY v.time DESC")
    List<Vote> getAllByDate(@Param("restaurantsId") int restaurantsId, @Param("date") LocalDate date);

    @Query("SELECT v from Vote v WHERE v.date >= :startDate AND v.date < :endDate ORDER BY v.date, v.time DESC")
    List<Vote> getBetweenHalfOpen(@Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);
}
