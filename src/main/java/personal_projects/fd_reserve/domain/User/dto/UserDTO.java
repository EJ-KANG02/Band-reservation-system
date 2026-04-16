package personal_projects.fd_reserve.domain.User.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import personal_projects.fd_reserve.domain.Officer.dto.OfficerDTO;
import personal_projects.fd_reserve.global.common.enums.Position;
import personal_projects.fd_reserve.global.common.enums.Role;
import personal_projects.fd_reserve.global.common.enums.Status;

import java.time.LocalDateTime;

public class UserDTO {

    public static class UserRequest{

        @Getter
        @NoArgsConstructor
        @AllArgsConstructor
        @Builder
        public static class LoginRequest{
            private String kakaoId;
        }

        @Getter
        @NoArgsConstructor
        @AllArgsConstructor
        @Builder
        public static class SignUpRequest{
            @NotBlank(message = "카카오 ID는 필수 입력 값입니다.")
            private String kakaoId;   // 카카오에서 제공하는 고유 식별값

            @NotBlank(message = "이름은 필수 입력 값입니다.")
            private String name;      // 사용자 본명

            @NotBlank(message = "닉네임은 필수 입력 값입니다.")
            private String nickname;  // 서비스에서 사용할 별명

            @NotBlank(message = "학번은 필수 입력 값입니다.")
            private String studentId; // 학번

            private String teamName;  // 소속 팀 이름

            @NotNull(message = "역할은 필수 입력 값입니다.")
            private Role role; //부원 or 회장단

            private OfficerDTO.OfficerRequest.officerSignUpRequest officerInfo; //회장단일 경우에만 데이터가 채워짐
        }

        @Getter
        @NoArgsConstructor
        @AllArgsConstructor
        @Builder
        public static class UpdateInfoRequest {
            private String nickname;  // optional
            private String name;      // optional
            private String studentId; // optional
            private String teamName; //optional
        }
    }

    public static class UserResponse{

        @Getter
        @NoArgsConstructor
        @AllArgsConstructor
        @Builder
        public static class UserInfoResponse {
            private String nickname;
            private String name;
            private String studentId;
            private String teamName;
            private String role;

            private Integer batch;
            private Position position;
        }
    }
}
