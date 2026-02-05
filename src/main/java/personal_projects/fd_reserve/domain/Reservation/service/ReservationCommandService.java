package personal_projects.fd_reserve.domain.Reservation.service;

import personal_projects.fd_reserve.domain.Reservation.dto.ReservationDTO;
import personal_projects.fd_reserve.domain.Reservation.entity.Reservation;
import personal_projects.fd_reserve.domain.User.entity.User;

public interface ReservationCommandService {
    Reservation createReservation(User user, ReservationDTO.ReservationRequest.CreateRequest request);
}
