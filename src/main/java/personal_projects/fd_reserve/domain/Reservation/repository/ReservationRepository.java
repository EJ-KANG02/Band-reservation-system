package personal_projects.fd_reserve.domain.Reservation.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import personal_projects.fd_reserve.domain.Reservation.entity.Reservation;

public interface ReservationRepository extends JpaRepository<Reservation, Long> {

}
