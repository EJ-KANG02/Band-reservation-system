package personal_projects.fd_reserve.domain.Reservation.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.*;
import personal_projects.fd_reserve.domain.User.entity.User;
import personal_projects.fd_reserve.global.common.enums.Category;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

public class ReservationDTO {

    public static class ReservationRequest{

        @Getter
        @NoArgsConstructor
        @AllArgsConstructor
        @Builder
        public static class CreateRequest{

            @NotBlank(message = "날짜는 필수 입력 값입니다.")
            private LocalDate date;

            @NotNull(message = "시작 시간은 필수 입력 값입니다.")
            @Pattern(regexp = "^[0-2][0-9]:[0-5][0-9]$", message = "시간 형식이 올바르지 않습니다. HH:mm 형식으로 입력해주세요.")
            private LocalTime startTime;

            @NotNull(message = "종료 시간은 필수 입력 값입니다.")
            @Pattern(regexp = "^[0-2][0-9]:[0-5][0-9]$", message = "시간 형식이 올바르지 않습니다. HH:mm 형식으로 입력해주세요.")
            private LocalTime endTime;

            @NotNull(message = "카테고리는 필수 입력 값입니다.")
            private Category category;
        }
    }

    public static class ReservationResponse{

        @Builder
        @Getter
        @NoArgsConstructor(access = AccessLevel.PROTECTED)
        @AllArgsConstructor(access = AccessLevel.PROTECTED)
        public static class CreateResponse{
            private Long reservationId;
            private String nickname;
            private LocalDateTime createdAt;
        }

    }
}
