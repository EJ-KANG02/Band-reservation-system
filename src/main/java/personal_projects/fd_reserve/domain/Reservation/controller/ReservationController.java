package personal_projects.fd_reserve.domain.Reservation.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import personal_projects.fd_reserve.domain.Reservation.converter.ReservationConverter;
import personal_projects.fd_reserve.domain.Reservation.dto.ReservationDTO;
import personal_projects.fd_reserve.domain.Reservation.entity.Reservation;
import personal_projects.fd_reserve.domain.Reservation.service.ReservationCommandService;
import personal_projects.fd_reserve.domain.Reservation.service.ReservationQueryService;
import personal_projects.fd_reserve.global.error.ApiResponse;

import java.time.LocalDate;
import java.util.List;

@Tag(name = "Reservation", description = "예약 관련 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v0/reservations")
@Validated
public class ReservationController {

    private final ReservationCommandService reservationCommandService;
    private final ReservationQueryService reservationQueryService;

    @Operation(summary = "Bulk 예약 생성 API",
            description = "여러 개의 예약 시간대를 리스트로 받아 한 번에 등록합니다. 하나라도 중복되거나 정책을 위반하면 전체가 롤백됩니다.")
    @PostMapping("")
    public ApiResponse<List<ReservationDTO.ReservationResponse.CreateResponse>> createReservation(
            @AuthenticationPrincipal UserDetails principal,
            @RequestBody @Valid ReservationDTO.ReservationRequest.BulkCreateDTO request
    ) {
        List<ReservationDTO.ReservationResponse.CreateResponse> results = reservationCommandService.createBulkReservations(principal, request);

        return ApiResponse.onSuccess(results);
    }

    @Operation(summary = "나의 활성화된 예약 조회 API",
            description = "현재 로그인한 유저가 신청한 예약 중, 아직 시간이 지나지 않은 유효한 예약 목록을 조회합니다.")
    @GetMapping("/active")
    public ApiResponse<ReservationDTO.ReservationResponse.ReservationListDTO> getActiveReservations(
            @AuthenticationPrincipal UserDetails principal) {

        ReservationDTO.ReservationResponse.ReservationListDTO response =
                reservationQueryService.getActiveReservationList(principal);
        return ApiResponse.onSuccess(response);
    }

    @Operation(summary = "주간 시간표 조회 API",
            description = "특정 날짜가 포함된 일주일(월~일) 전체의 예약 현황을 조회합니다. 대시보드나 전체 시간표 뷰에 사용합니다.")
    @GetMapping("/weekly")
    public ApiResponse<ReservationDTO.ReservationResponse.WeeklyTimetableDTO> getWeeklyTimetable(
            @Parameter(description = "조회하고자 하는 주에 포함된 날짜 (yyyy-MM-dd)", example = "2026-04-07")
            @RequestParam(name = "date") @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate date
    ) {
        ReservationDTO.ReservationResponse.WeeklyTimetableDTO response = reservationQueryService.getWeeklyTimetable(date);
        return ApiResponse.onSuccess(response);
    }

    @Operation(summary = "일자별 예약된 시간대 조회 API",
            description = "특정 날짜에 이미 예약이 완료된 시간 덩어리들을 조회합니다. 드래그 UI에서 'X' 표시(비활성화)를 하기 위해 사용합니다.")
    @GetMapping("/check-availability")
    public ApiResponse<List<ReservationDTO.ReservationResponse.ReservedTimeDTO>> getReservedTimes(
            @Parameter(description = "확인하고자 하는 날짜 (yyyy-MM-dd)", example = "2026-04-07")
            @RequestParam(name = "date") @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate date
    ) {
        List<ReservationDTO.ReservationResponse.ReservedTimeDTO> reservedTimes = reservationQueryService.getReservedTimeByDate(date);
        return ApiResponse.onSuccess(reservedTimes);
    }

    @Operation(summary = "예약 수정 API",
            description = "기존 예약의 날짜, 시간, 카테고리를 변경합니다. 본인의 예약만 수정 가능합니다.")
    @PatchMapping("/{reservationId}")
    public ApiResponse<ReservationDTO.ReservationResponse.UpdateResponse> updateReservation(
            @AuthenticationPrincipal UserDetails principal,
            @Parameter(description = "수정할 예약의 ID", example = "10")
            @PathVariable(name = "reservationId") Long reservationId,
            @RequestBody @Valid ReservationDTO.ReservationRequest.UpdateRequest request
    ) {
        ReservationDTO.ReservationResponse.UpdateResponse result = reservationCommandService.updateReservation(principal, reservationId, request);

        return ApiResponse.onSuccess(result);
    }

    @Operation(summary = "예약 삭제 API",
            description = "특정 예약을 삭제합니다. 본인의 예약만 삭제 가능합니다.")
    @DeleteMapping("/{reservationId}")
    public ApiResponse<String> deleteReservation(
            @AuthenticationPrincipal UserDetails principal,
            @Parameter(description = "삭제할 예약의 ID", example = "10")
            @PathVariable(name = "reservationId") Long reservationId
    ) {
        reservationCommandService.deleteReservation(principal, reservationId);
        return ApiResponse.onSuccess("예약이 성공적으로 삭제되었습니다.");
    }

}
