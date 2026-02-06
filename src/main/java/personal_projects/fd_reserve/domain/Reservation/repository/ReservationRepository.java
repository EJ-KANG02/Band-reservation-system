package personal_projects.fd_reserve.domain.Reservation.repository;

import org.springframework.cglib.core.Local;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import personal_projects.fd_reserve.domain.Reservation.entity.Reservation;

import java.time.LocalDate;
import java.time.LocalTime;

public interface ReservationRepository extends JpaRepository<Reservation, Long> {

    @Query("SELECT EXISTS (SELECT 1 FROM Reservation r " +
            "WHERE r.date = :date " +
            "AND r.startTime < :endTime " +
            "AND r.endTime > :startTime)")
    boolean existOverlappingReservation(@Param("date")LocalDate date,
                                        @Param("startTime")LocalTime startTime,
                                        @Param("endTime")LocalTime endTime);
}
