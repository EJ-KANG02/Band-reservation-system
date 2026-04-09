package personal_projects.fd_reserve.domain.Reservation.repository;

import org.springframework.cglib.core.Local;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.security.core.parameters.P;
import personal_projects.fd_reserve.domain.Reservation.entity.Reservation;
import personal_projects.fd_reserve.domain.User.entity.User;
import personal_projects.fd_reserve.global.common.enums.Category;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public interface ReservationRepository extends JpaRepository<Reservation, Long> {

    List<Reservation> findAllByDate(LocalDate date);

    @Query("SELECT EXISTS (SELECT 1 FROM Reservation r " +
            "WHERE r.date = :date " +
            "AND r.startTime < :endTime " +
            "AND r.endTime > :startTime)")
    boolean existOverlappingReservation(@Param("date")LocalDate date,
                                        @Param("startTime")LocalTime startTime,
                                        @Param("endTime")LocalTime endTime);

    @Query("SELECT r FROM Reservation r " +
            "WHERE r.date > :nowDate OR (r.date = :nowDate AND r.endTime > :nowTime) " +
            "ORDER BY r.date ASC, r.startTime ASC ")
    List<Reservation> findAllActiveReservations(@Param("nowDate") LocalDate nowDate, @Param("nowTime") LocalTime nowTime);

    @Query("SELECT r FROM Reservation r " +
            "WHERE r.date BETWEEN :startDate AND :endDate " +
            "ORDER BY r.date ASC, r.startTime ASC ")
    List<Reservation> findAllByDateBetween(LocalDate startDate, LocalDate endDate);


    @Query("SELECT COUNT(r) FROM Reservation r " +
            "WHERE r.user.teamName = :teamName " +
            "AND r.category = :category " +
            "AND r.date BETWEEN :startDate AND :endDate")
    long countByTeamNameAndCategoryAndDateBetween(
            @Param("teamName") String teamName,
            @Param("category") Category category,
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate
    );

    @Query("SELECT COUNT(r) FROM Reservation r " +
            "WHERE r.user = :user " +
            "AND r.category = :category " +
            "AND r.date BETWEEN :startDate AND :endDate")
    long countByUserAndCategoryAndDateBetween(
            @Param("user") User user,
            @Param("category") Category category,
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate
    );

    @Query("SELECT COUNT(r) > 0 FROM Reservation r "+
            "WHERE r.date = :date "+
            "AND r.id <> :excludeId "+
            "AND r.startTime < :endTime "+
            "AND r.endTime > :startTime")
    boolean existsOverlappingExceptSelf(
            @Param("date") LocalDate date,
            @Param("startTime") LocalTime startTime,
            @Param("endTime") LocalTime endTime,
            @Param("excludeId") Long excludeId
    );
}
