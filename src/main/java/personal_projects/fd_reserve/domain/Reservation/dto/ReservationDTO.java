package personal_projects.fd_reserve.domain.Reservation.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.*;
import personal_projects.fd_reserve.domain.User.entity.User;
import personal_projects.fd_reserve.global.common.enums.Category;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

public class ReservationDTO {

    public static class ReservationRequest{

        @Builder
        @Getter
        @NoArgsConstructor(access = AccessLevel.PROTECTED)
        @AllArgsConstructor(access = AccessLevel.PROTECTED)
        public static class CreateRequest{

            @NotNull(message = "날짜는 필수 입력 값입니다.")
            @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
            private LocalDate date;

            @NotNull(message = "시작 시간은 필수 입력 값입니다.")
            @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm")
            private LocalTime startTime;

            @NotNull(message = "종료 시간은 필수 입력 값입니다.")
            @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm")
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


        @Builder
        @Getter
        @NoArgsConstructor(access = AccessLevel.PROTECTED)
        @AllArgsConstructor(access = AccessLevel.PROTECTED)
        public static class ReservationDetailDTO {
            private Long reservationId;
            private String nickname;
            private String teamName;
            private LocalDate date;
            private LocalTime startTime;
            private LocalTime endTime;
            private Category category;
            private boolean isOngoing;
        }

        @Builder
        @Getter
        @NoArgsConstructor(access = AccessLevel.PROTECTED)
        @AllArgsConstructor(access = AccessLevel.PROTECTED)
        public static class ReservationListDTO {
            private List<ReservationDetailDTO> reservationList;
        }
    }
}
