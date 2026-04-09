package personal_projects.fd_reserve.domain.Reservation.service;

import org.springframework.security.core.userdetails.UserDetails;
import personal_projects.fd_reserve.domain.Reservation.dto.ReservationDTO;
import personal_projects.fd_reserve.domain.Reservation.entity.Reservation;
import personal_projects.fd_reserve.domain.Setting.entity.Setting;
import personal_projects.fd_reserve.domain.User.entity.User;

import java.util.List;

public interface ReservationCommandService {
    List<ReservationDTO.ReservationResponse.CreateResponse> createBulkReservations(UserDetails principal, ReservationDTO.ReservationRequest.BulkCreateDTO request);
    ReservationDTO.ReservationResponse.UpdateResponse updateReservation(UserDetails principal, Long reservationId, ReservationDTO.ReservationRequest.UpdateRequest request);
}
