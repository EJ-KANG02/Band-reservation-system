package personal_projects.fd_reserve.domain.Reservation.repository;

import org.springframework.cglib.core.Local;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import personal_projects.fd_reserve.domain.Reservation.entity.Reservation;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public interface ReservationRepository extends JpaRepository<Reservation, Long> {

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

}
