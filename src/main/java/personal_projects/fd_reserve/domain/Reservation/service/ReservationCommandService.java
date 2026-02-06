package personal_projects.fd_reserve.domain.Reservation.service;

import org.springframework.security.core.userdetails.UserDetails;
import personal_projects.fd_reserve.domain.Reservation.dto.ReservationDTO;
import personal_projects.fd_reserve.domain.Reservation.entity.Reservation;

public interface ReservationCommandService {
    Reservation createReservation(UserDetails principal, ReservationDTO.ReservationRequest.CreateRequest request);
}
