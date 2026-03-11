package personal_projects.fd_reserve.domain.Reservation.service;

import personal_projects.fd_reserve.domain.Reservation.dto.ReservationDTO;


public interface ReservationQueryService {
    ReservationDTO.ReservationResponse.ReservationListDTO getActiveReservationList();
}
