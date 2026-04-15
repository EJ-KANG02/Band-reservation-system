package personal_projects.fd_reserve.domain.Reservation.service.impl;

import org.springframework.boot.web.error.Error;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import personal_projects.fd_reserve.domain.Reservation.converter.ReservationConverter;
import personal_projects.fd_reserve.domain.Reservation.dto.ReservationDTO;
import personal_projects.fd_reserve.domain.Reservation.entity.Reservation;
import personal_projects.fd_reserve.domain.Reservation.repository.ReservationRepository;
import personal_projects.fd_reserve.domain.Reservation.service.ReservationCommandService;
import personal_projects.fd_reserve.domain.Setting.entity.Setting;
import personal_projects.fd_reserve.domain.Setting.repository.SettingRepository;
import personal_projects.fd_reserve.domain.User.entity.User;
import personal_projects.fd_reserve.domain.User.repository.UserRepository;
import personal_projects.fd_reserve.global.common.enums.Category;
import personal_projects.fd_reserve.global.error.code.status.ErrorStatus;
import personal_projects.fd_reserve.global.error.handler.ReservationException;
import personal_projects.fd_reserve.global.error.handler.SettingException;
import personal_projects.fd_reserve.global.error.handler.UserException;

import java.time.DayOfWeek;
import java.time.Duration;
import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class ReservationCommandServiceImpl implements ReservationCommandService {
    private final ReservationRepository reservationRepository;
    private final SettingRepository settingRepository;
    private final UserRepository userRepository;

    @Override
    public List<ReservationDTO.ReservationResponse.CreateResponse> createBulkReservations(UserDetails principal, ReservationDTO.ReservationRequest.BulkCreateDTO request) {
        String kakaoId = principal.getUsername();

        //스프링 시큐리티 객체에서 꺼낸 kakaoId로 User 객체 찾아주기
        User user = userRepository.findByKakaoId(kakaoId)
                .orElseThrow(()-> new UserException(ErrorStatus.USER_NOT_FOUND));

        //설정 정보 가져오기
        Setting setting = settingRepository.findById(1L)
                .orElseThrow(() -> new SettingException(ErrorStatus.SETTING_NOT_FOUND));

        List<ReservationDTO.ReservationResponse.CreateResponse> results = new ArrayList<>();

        // 현재의 벌크 요청 내에서 카테고리별로 몇 개를 승인했는지 추적하는 카운터
        int ensembleCountInRequest = 0;
        int drumCountInRequest = 0;

        for (ReservationDTO.ReservationRequest.CreateRequest singleRequest : request.getReservationList()){

            validateSingleReservation(user, singleRequest, setting,
                    singleRequest.getCategory() == Category.ENSEMBLE ? ensembleCountInRequest : drumCountInRequest);

            // 검증 통과 시 카운트 증가
            if (singleRequest.getCategory() == Category.ENSEMBLE) ensembleCountInRequest++;
            else if (singleRequest.getCategory() == Category.DRUM) drumCountInRequest++;

            Reservation reservation = ReservationConverter.toReservation(user, singleRequest);
            Reservation saved = reservationRepository.save(reservation);

            results.add(ReservationConverter.toCreateResult(saved));
        }

        return results;
    }

    private void validateSingleReservation(User user, ReservationDTO.ReservationRequest.CreateRequest request, Setting setting, int currentRequestCount) {

        // 시작 < 종료 검증
        if (request.getEndTime().isBefore(request.getStartTime())) {
            throw new ReservationException(ErrorStatus.INVALID_RESERVATION_TIME);
        }

        LocalDate startOfWeek = request.getDate().with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY));
        LocalDate endOfWeek = request.getDate().with(TemporalAdjusters.nextOrSame(DayOfWeek.SUNDAY));

        // 요청한 예약의 분 단위 시간
        long requestedMinutes = Duration.between(request.getStartTime(), request.getEndTime()).toMinutes();

        if (request.getCategory() == Category.ENSEMBLE) {
            if (user.getTeamName() == null || user.getTeamName().isBlank()) {
                throw new ReservationException(ErrorStatus.TEAM_NAME_NOT_FOUND);
            }

            // 회당 시간 제한 (단일 예약이 maxTime 초과하는지)
            if (requestedMinutes > (setting.getEnsembleMaxTime() * 60)) {
                throw new ReservationException(ErrorStatus.EXCEEDED_TIME_LIMIT);
            }

            String teamName = user.getTeamName();

            // 주당 횟수 제한
            long weeklyCount = reservationRepository.countByTeamNameAndCategoryAndDateBetween(
                    teamName, Category.ENSEMBLE, startOfWeek, endOfWeek
            ) + currentRequestCount;

            if (weeklyCount >= setting.getEnsembleMaxCountPerWeek()) {
                throw new ReservationException(ErrorStatus.EXCEEDED_WEEKLY_COUNT_LIMIT);
            }

            // ENSEMBLE 하루 총 시간 체크
            long dailyUsedMinutes = calculateDailyUsedMinutes(
                    reservationRepository.findByTeamNameAndCategoryAndDate(
                            teamName, Category.ENSEMBLE, request.getDate()
                    )
            );

            if (dailyUsedMinutes + requestedMinutes > (setting.getEnsembleMaxTime() * 60)) {
                throw new ReservationException(ErrorStatus.EXCEEDED_TIME_LIMIT);
            }

        } else if (request.getCategory() == Category.DRUM) {

            // 회당 시간 제한
            if (requestedMinutes > (setting.getDrumMaxTime() * 60)) {
                throw new ReservationException(ErrorStatus.EXCEEDED_TIME_LIMIT);
            }

            // 주당 횟수 제한
            long weeklyCount = reservationRepository.countByUserAndCategoryAndDateBetween(
                    user, Category.DRUM, startOfWeek, endOfWeek
            ) + currentRequestCount;

            if (weeklyCount >= setting.getDrumMaxCountPerWeek()) {
                throw new ReservationException(ErrorStatus.EXCEEDED_WEEKLY_COUNT_LIMIT);
            }

            // DRUM 하루 총 시간 체크
            long dailyUsedMinutes = calculateDailyUsedMinutes(
                    reservationRepository.findByUserAndCategoryAndDate(
                            user, Category.DRUM, request.getDate()
                    )
            );

            if (dailyUsedMinutes + requestedMinutes > (setting.getDrumMaxTime() * 60)) {
                throw new ReservationException(ErrorStatus.EXCEEDED_TIME_LIMIT);
            }
        }

        // 시간 중복 검증
        boolean isOverlapping = reservationRepository.existOverlappingReservation(
                request.getDate(),
                request.getStartTime(),
                request.getEndTime()
        );

        if (isOverlapping) {
            throw new ReservationException(ErrorStatus.RESERVATION_TIME_CONFLICT);
        }
    }

    public ReservationDTO.ReservationResponse.UpdateResponse updateReservation(
            UserDetails principal, Long reservationId, ReservationDTO.ReservationRequest.UpdateRequest request
    ){
        String kakaoId = principal.getUsername();

        //스프링 시큐리티 객체에서 꺼낸 kakaoId로 User 객체 찾아주기
        User user = userRepository.findByKakaoId(kakaoId)
                .orElseThrow(()-> new UserException(ErrorStatus.USER_NOT_FOUND));

        Reservation reservation = reservationRepository.findById(reservationId)
                .orElseThrow(() -> new ReservationException(ErrorStatus.RESERVATION_NOT_FOUND));

        if (!reservation.getUser().getId().equals(user.getId())) {
            throw new ReservationException(ErrorStatus.RESERVATION_NOT_OWNER);
        }

        Setting setting = settingRepository.findById(1L)
                .orElseThrow(() -> new SettingException(ErrorStatus.SETTING_NOT_FOUND));

        validateUpdateReservation(reservation, request, setting);

        reservation.update(request.getDate(), request.getStartTime(), request.getEndTime(), request.getCategory());

        return ReservationConverter.toUpdateResult(reservation);
    }

    private void validateUpdateReservation(Reservation reservation, ReservationDTO.ReservationRequest.UpdateRequest request, Setting setting){

        //예약 시작 시각 < 종료 시각 검증
        if (request.getEndTime().isBefore(request.getStartTime())){
            throw new ReservationException(ErrorStatus.INVALID_RESERVATION_TIME);
        }

        //카테고리별 이용 시간 검증
        long requestedMinutes = Duration.between(request.getStartTime(), request.getEndTime()).toMinutes();
        int maxTime = (request.getCategory() == Category.ENSEMBLE) ? setting.getEnsembleMaxTime() : setting.getDrumMaxTime();

        if (requestedMinutes > (maxTime * 60)) {
            throw new ReservationException(ErrorStatus.EXCEEDED_TIME_LIMIT);
        }

        //시간 중복 검증
        boolean isOverlapping = reservationRepository.existsOverlappingExceptSelf(
                request.getDate(),
                request.getStartTime(),
                request.getEndTime(),
                reservation.getId() // 현재 수정 중인 예약 ID 전달
        );

        if (isOverlapping) {
            throw new ReservationException(ErrorStatus.RESERVATION_TIME_CONFLICT);
        }
    }

    public void deleteReservation(UserDetails principal, Long reservationId) {
        String kakaoId = principal.getUsername();

        //유저 확인
        User user = userRepository.findByKakaoId(kakaoId)
                .orElseThrow(() -> new UserException(ErrorStatus.USER_NOT_FOUND));

        //삭제할 예약 조회
        Reservation reservation = reservationRepository.findById(reservationId)
                .orElseThrow(() -> new ReservationException(ErrorStatus.RESERVATION_NOT_FOUND));

        //권한 체크 (본인 확인)
        if (!reservation.getUser().getId().equals(user.getId())) {
            throw new ReservationException(ErrorStatus.RESERVATION_NOT_OWNER);
        }

        //삭제 실행
        reservationRepository.delete(reservation);
    }

    private long calculateDailyUsedMinutes(List<Reservation> reservations) {
        return reservations.stream()
                .mapToLong(r -> Duration.between(r.getStartTime(), r.getEndTime()).toMinutes())
                .sum();
    }
}
