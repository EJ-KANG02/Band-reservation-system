package personal_projects.fd_reserve.domain.Reservation.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import personal_projects.fd_reserve.domain.Reservation.converter.ReservationConverter;
import personal_projects.fd_reserve.domain.Reservation.dto.ReservationDTO;
import personal_projects.fd_reserve.domain.Reservation.entity.Reservation;
import personal_projects.fd_reserve.domain.Reservation.service.ReservationCommandService;
import personal_projects.fd_reserve.domain.User.entity.User;
import personal_projects.fd_reserve.global.error.ApiResponse;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v0/reservations")
@Validated
public class ReservationController {

    private final ReservationCommandService reservationCommandService;

    @PostMapping("")
    public ApiResponse<ReservationDTO.ReservationResponse.CreateResponse> createReservation(
            @AuthenticationPrincipal User principal,
            @RequestBody @Valid ReservationDTO.ReservationRequest.CreateRequest request
    ) {
        Reservation reservation = reservationCommandService.createReservation(principal, request);

        return ApiResponse.onSuccess(ReservationConverter.toCreateResult(reservation));
    }
}
