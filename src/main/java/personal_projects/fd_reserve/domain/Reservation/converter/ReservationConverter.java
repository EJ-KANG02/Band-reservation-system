package personal_projects.fd_reserve.domain.Reservation.converter;

import personal_projects.fd_reserve.domain.Reservation.dto.ReservationDTO;
import personal_projects.fd_reserve.domain.Reservation.entity.Reservation;
import personal_projects.fd_reserve.domain.User.entity.User;
import personal_projects.fd_reserve.global.common.enums.Category;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public class ReservationConverter {

    public static Reservation toReservation(User user, ReservationDTO.ReservationRequest.CreateRequest request){
        Reservation newReservation = Reservation.builder()
                .user(user)
                .date(request.getDate())
                .startTime(request.getStartTime())
                .endTime(request.getEndTime())
                .category(request.getCategory())
                .build();
        return newReservation;
    }

    public static ReservationDTO.ReservationResponse.CreateResponse toCreateResult(Reservation reservation){
        return ReservationDTO.ReservationResponse.CreateResponse.builder()
                .reservationId(reservation.getId())
                .nickname(reservation.getUser().getNickname())
                .createdAt(reservation.getCreatedAt())
                .build();
    }

    public static ReservationDTO.ReservationResponse.ReservationDetailDTO toReservationDetailDTO(Reservation reservation, boolean isOngoing) {
        return ReservationDTO.ReservationResponse.ReservationDetailDTO.builder()
                .reservationId(reservation.getId())
                .nickname(reservation.getUser().getNickname())
                .teamName(reservation.getCategory() == Category.DRUM ? "-" : reservation.getUser().getTeamName())
                .date(reservation.getDate())
                .startTime(reservation.getStartTime())
                .endTime(reservation.getEndTime())
                .category(reservation.getCategory())
                .isOngoing(isOngoing)
                .build();
    }

    public static ReservationDTO.ReservationResponse.WeeklyReservationDetailDTO toWeeklyReservationDetailDTO(Reservation reservation) {
        return ReservationDTO.ReservationResponse.WeeklyReservationDetailDTO.builder()
                .reservationId(reservation.getId())
                .nickname(reservation.getUser().getNickname())
                .teamName(reservation.getCategory() == Category.DRUM ? "-" : reservation.getUser().getTeamName())
                .date(reservation.getDate())
                .startTime(reservation.getStartTime())
                .endTime(reservation.getEndTime())
                .category(reservation.getCategory())
                .build();
    }

    public static ReservationDTO.ReservationResponse.ReservedTimeDTO toReservedTimeDTO(Reservation reservation) {
        return ReservationDTO.ReservationResponse.ReservedTimeDTO.builder()
                .startTime(reservation.getStartTime())
                .endTime(reservation.getEndTime())
                .build();
    }

    public static ReservationDTO.ReservationResponse.UpdateResponse toUpdateResult(Reservation reservation) {
        return ReservationDTO.ReservationResponse.UpdateResponse.builder()
                .reservationId(reservation.getId())
                .date(reservation.getDate())
                .startTime(reservation.getStartTime())
                .endTime(reservation.getEndTime())
                .category(reservation.getCategory().name())
                .updatedAt(LocalDateTime.now())
                .build();
    }
}
