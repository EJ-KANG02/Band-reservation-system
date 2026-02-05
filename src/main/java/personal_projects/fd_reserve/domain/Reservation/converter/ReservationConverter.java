package personal_projects.fd_reserve.domain.Reservation.converter;

import personal_projects.fd_reserve.domain.Reservation.dto.ReservationDTO;
import personal_projects.fd_reserve.domain.Reservation.entity.Reservation;
import personal_projects.fd_reserve.domain.User.entity.User;

import java.time.LocalDateTime;

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
}
