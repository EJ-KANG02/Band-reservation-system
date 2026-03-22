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
        //예약 시작 시각 < 종료 시각 검증
        if (request.getEndTime().isBefore(request.getStartTime())){
            throw new ReservationException(ErrorStatus.INVALID_RESERVATION_TIME);
        }

        LocalDate startOfWeek = request.getDate().with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY));
        LocalDate endOfWeek = request.getDate().with(TemporalAdjusters.nextOrSame(DayOfWeek.SUNDAY));

        //이용 시간 계산 및 검증
        long requestedMinutes = Duration.between(request.getStartTime(), request.getEndTime()).toMinutes();

        //카테고리가 합주일 때, 팀이 존재하는지 검증
        if (request.getCategory() == Category.ENSEMBLE){
            if (user.getTeamName() == null || user.getTeamName().isBlank()) {
                throw new ReservationException(ErrorStatus.TEAM_NAME_NOT_FOUND);
            }

            //회당 시간 제한 체크
            if (requestedMinutes > (setting.getEnsembleMaxTime() * 60)) {
                throw new ReservationException(ErrorStatus.EXCEEDED_TIME_LIMIT);
            }

            String teamName = user.getTeamName();

            //주당 횟수 제한 체크 ( + 현재 트랜잭션 내에서 등록된 예약 개수)
            long count = reservationRepository.countByTeamNameAndCategoryAndDateBetween(
                    teamName, Category.ENSEMBLE, startOfWeek, endOfWeek
            ) + currentRequestCount;

            if (count >= setting.getEnsembleMaxCountPerWeek()) {
                throw new ReservationException(ErrorStatus.EXCEEDED_WEEKLY_COUNT_LIMIT);
            }
        }

        //카테고리가 드럼인 경우
        else if (request.getCategory() == Category.DRUM){

            //회당 시간 제한 체크
            if (requestedMinutes > (setting.getDrumMaxTime() * 60)) {
                throw new ReservationException(ErrorStatus.EXCEEDED_TIME_LIMIT);
            }

            //주당 횟수 제한 체크
            long count = reservationRepository.countByUserAndCategoryAndDateBetween(
                    user, Category.DRUM, startOfWeek, endOfWeek
            ) + currentRequestCount;

            if (count >= setting.getDrumMaxCountPerWeek()) {
                throw new ReservationException(ErrorStatus.EXCEEDED_WEEKLY_COUNT_LIMIT);
            }
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
    }

}
