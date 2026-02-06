package personal_projects.fd_reserve.domain.Reservation.service.impl;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import personal_projects.fd_reserve.domain.Reservation.converter.ReservationConverter;
import personal_projects.fd_reserve.domain.Reservation.dto.ReservationDTO;
import personal_projects.fd_reserve.domain.Reservation.entity.Reservation;
import personal_projects.fd_reserve.domain.Reservation.repository.ReservationRepository;
import personal_projects.fd_reserve.domain.Reservation.service.ReservationCommandService;
import personal_projects.fd_reserve.domain.User.entity.User;
import personal_projects.fd_reserve.global.common.enums.Category;
import personal_projects.fd_reserve.global.error.code.status.ErrorStatus;
import personal_projects.fd_reserve.global.error.handler.ReservationException;

@Service
@RequiredArgsConstructor
@Transactional
public class ReservationCommandServiceImpl implements ReservationCommandService {
    private final ReservationRepository reservationRepository;

    @Override
    public Reservation createReservation(User user, ReservationDTO.ReservationRequest.CreateRequest request){

        //카테고리가 합주일 때, 팀이 존재하는지 검증
        if (request.getCategory() == Category.ENSEMBLE){
            if (user.getTeamName() == null || user.getTeamName().isBlank()) {
                throw new ReservationException(ErrorStatus.TEAM_NAME_NOT_FOUND);
            }
        }

        if (request.getEndTime().isBefore(request.getStartTime())){
            throw new ReservationException(ErrorStatus.INVALID_RESERVATION_TIME);
        }

        boolean isOverlapping = reservationRepository.existOverlappingReservation(
                request.getDate(),
                request.getStartTime(),
                request.getEndTime()
        );

        //기존 시간표와 충돌 여부 검증
        if (isOverlapping) {
            throw new ReservationException(ErrorStatus.RESERVATION_TIME_CONFLICT);
        }

        Reservation newReservation = ReservationConverter.toReservation(user, request);
        return newReservation;
    }


}
