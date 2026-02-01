package personal_projects.fd_reserve.domain.Officer.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import personal_projects.fd_reserve.global.common.enums.Position;
import personal_projects.fd_reserve.global.common.enums.Role;

public class OfficerDTO {

    public static class OfficerRequest {
        @Getter
        @NoArgsConstructor
        @AllArgsConstructor
        @Builder
        public static class officerSignUpRequest{
            @NotNull(message = "기수는 필수 입력 값입니다.")
            @Positive(message = "기수는 양수여야 합니다.")
            private Integer batch;   // 기수

            @NotNull(message = "임원 역할은 필수 입력 값입니다.")
            private Position position;
        }
    }
}
