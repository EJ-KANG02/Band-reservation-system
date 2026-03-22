package personal_projects.fd_reserve.domain.Reservation.service.impl;

import org.springframework.cglib.core.Local;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import personal_projects.fd_reserve.domain.Reservation.converter.ReservationConverter;
import personal_projects.fd_reserve.domain.Reservation.dto.ReservationDTO;
import personal_projects.fd_reserve.domain.Reservation.repository.ReservationRepository;
import personal_projects.fd_reserve.domain.Reservation.service.ReservationQueryService;
import personal_projects.fd_reserve.domain.Reservation.entity.Reservation;


import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.TemporalAdjusters;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ReservationQueryServiceImpl implements ReservationQueryService {

    private final ReservationRepository reservationRepository;

    @Override
    public ReservationDTO.ReservationResponse.ReservationListDTO getActiveReservationList() {
        LocalDateTime now = LocalDateTime.now();
        LocalDate nowDate = now.toLocalDate();
        LocalTime nowTime = now.toLocalTime();

        List<Reservation> reservations = reservationRepository.findAllActiveReservations(nowDate, nowTime);

        List<ReservationDTO.ReservationResponse.ReservationDetailDTO> detailDTOs = reservations.stream()
                .map(reservation -> {
                    boolean isOngoing = checkIfOngoing(reservation,nowDate, nowTime);
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
