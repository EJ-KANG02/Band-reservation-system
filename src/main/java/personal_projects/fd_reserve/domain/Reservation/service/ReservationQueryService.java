package personal_projects.fd_reserve.domain.Reservation.service;

import personal_projects.fd_reserve.domain.Reservation.dto.ReservationDTO;

import java.time.LocalDate;
import java.util.List;


public interface ReservationQueryService {
    ReservationDTO.ReservationResponse.ReservationListDTO getActiveReservationList();
    ReservationDTO.ReservationResponse.WeeklyTimetableDTO getWeeklyTimetable(LocalDate selectedDate);
    List<ReservationDTO.ReservationResponse.ReservedTimeDTO> getReservedTimeByDate(LocalDate date);
}
