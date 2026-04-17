package personal_projects.fd_reserve.domain.Reservation.service.impl;

import org.springframework.cglib.core.Local;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import personal_projects.fd_reserve.domain.Reservation.converter.ReservationConverter;
import personal_projects.fd_reserve.domain.Reservation.dto.ReservationDTO;
import personal_projects.fd_reserve.domain.Reservation.repository.ReservationRepository;
import personal_projects.fd_reserve.domain.Reservation.service.ReservationQueryService;
import personal_projects.fd_reserve.domain.Reservation.entity.Reservation;
import personal_projects.fd_reserve.domain.User.entity.User;
import personal_projects.fd_reserve.domain.User.repository.UserRepository;
import personal_projects.fd_reserve.global.error.code.status.ErrorStatus;
import personal_projects.fd_reserve.global.error.handler.UserException;


import java.time.*;
import java.time.temporal.TemporalAdjusters;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ReservationQueryServiceImpl implements ReservationQueryService {

    private final ReservationRepository reservationRepository;
    private final UserRepository userRepository;

    @Override
    public ReservationDTO.ReservationResponse.ReservationListDTO getActiveReservationList(UserDetails principal) {
        LocalDateTime now = LocalDateTime.now(ZoneId.of("Asia/Seoul"));
        LocalDate nowDate = now.toLocalDate();
        LocalTime nowTime = now.toLocalTime();

        String kakaoId = principal.getUsername();

        //스프링 시큐리티 객체에서 꺼낸 kakaoId로 User 객체 찾아주기
        User user = userRepository.findByKakaoId(kakaoId)
                .orElseThrow(()-> new UserException(ErrorStatus.USER_NOT_FOUND));

        Long userId = user.getId();
        String teamName = user.getTeamName();

        List<Reservation> reservations = reservationRepository.findActiveReservationsForUser(nowDate, nowTime, userId, teamName);

        List<ReservationDTO.ReservationResponse.ReservationDetailDTO> detailDTOs = reservations.stream()
                .map(reservation -> {
                    boolean isOngoing = checkIfOngoing(reservation, nowDate, nowTime);
                    return ReservationConverter.toReservationDetailDTO(reservation, isOngoing);
                })
                .collect(Collectors.toList());

        return ReservationDTO.ReservationResponse.ReservationListDTO.builder()
                .reservationList(detailDTOs)
                .build();
    }

    private boolean checkIfOngoing(Reservation r, LocalDate nowDate, LocalTime nowTime) {
        if (!r.getDate().equals(nowDate)) return false;

        //시작 시각 <= 현재 시각 < 종료 시각
        return (r.getStartTime().isBefore(nowTime) || r.getStartTime().equals(nowTime))
                && r.getEndTime().isAfter(nowTime);
    }

    public ReservationDTO.ReservationResponse.WeeklyTimetableDTO getWeeklyTimetable(LocalDate selectedDate) {
        LocalDate startOfWeek = selectedDate.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY));
        LocalDate endOfWeek = selectedDate.with(TemporalAdjusters.nextOrSame(DayOfWeek.SUNDAY));
        List<Reservation> weeklyReservations = reservationRepository.findAllByDateBetween(startOfWeek, endOfWeek);

        Map<LocalDate, List<ReservationDTO.ReservationResponse.WeeklyReservationDetailDTO>> timetable = weeklyReservations.stream()
                .map(ReservationConverter::toWeeklyReservationDetailDTO)
                .collect(Collectors.groupingBy(ReservationDTO.ReservationResponse.WeeklyReservationDetailDTO::getDate));

        return ReservationDTO.ReservationResponse.WeeklyTimetableDTO.builder()
                .startDate(startOfWeek)
                .endDate(endOfWeek)
                .timetable(timetable)
                .build();
    }

    public List<ReservationDTO.ReservationResponse.ReservedTimeDTO> getReservedTimeByDate(LocalDate date){
        List<Reservation> reservations = reservationRepository.findAllByDate(date);

        return reservations.stream()
                .map(ReservationConverter::toReservedTimeDTO)
                .collect(Collectors.toList());
    }
}
