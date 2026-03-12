package personal_projects.fd_reserve.domain.Reservation.controller;

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

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v0/reservations")
@Validated
public class ReservationController {

    private final ReservationCommandService reservationCommandService;
    private final ReservationQueryService reservationQueryService;

    @PostMapping("")
    public ApiResponse<ReservationDTO.ReservationResponse.CreateResponse> createReservation(
            @AuthenticationPrincipal UserDetails principal,
            @RequestBody @Valid ReservationDTO.ReservationRequest.CreateRequest request
    ) {
        Reservation reservation = reservationCommandService.createReservation(principal, request);

        return ApiResponse.onSuccess(ReservationConverter.toCreateResult(reservation));
    }

    @GetMapping("/active")
    public ApiResponse<ReservationDTO.ReservationResponse.ReservationListDTO> getActiveReservations() {
        ReservationDTO.ReservationResponse.ReservationListDTO response = reservationQueryService.getActiveReservationList();
        return ApiResponse.onSuccess(response);
    }

    @GetMapping("/weekly")
    public ApiResponse<ReservationDTO.ReservationResponse.WeeklyTimetableDTO> getWeeklyTimetable(
            @RequestParam(name = "date") @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate date
    ) {
        ReservationDTO.ReservationResponse.WeeklyTimetableDTO response = reservationQueryService.getWeeklyTimetable(date);
        return ApiResponse.onSuccess(response);
    }

}
