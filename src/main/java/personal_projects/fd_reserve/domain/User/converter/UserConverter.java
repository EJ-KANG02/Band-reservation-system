package personal_projects.fd_reserve.domain.User.converter;

import personal_projects.fd_reserve.domain.Officer.entity.Officer;
import personal_projects.fd_reserve.domain.User.dto.UserDTO;
import personal_projects.fd_reserve.domain.User.entity.User;

public class UserConverter {

    public static User toUser(UserDTO.UserRequest.SignUpRequest request) {
        User newUser = User.builder()
                .kakaoId(request.getKakaoId())
                .name(request.getName())
                .nickname(request.getNickname())
                .studentId(request.getStudentId())
                .teamName(request.getTeamName())
                .role(request.getRole()) // "MEMBER" 또는 "OFFICER" 구분 저장
                .build();
        return newUser;
    }

    public static UserDTO.UserResponse.UserInfoResponse toUserInfoResponse(User user, Officer officer) {
        return UserDTO.UserResponse.UserInfoResponse.builder()
                .nickname(user.getNickname())
                .name(user.getName())
                .studentId(user.getStudentId())
                .teamName(user.getTeamName())
                .role(user.getRole().name())
                .batch(officer != null ? officer.getBatch() : null)
                .position(officer != null ? officer.getPosition() : null)
                .build();
    }
}
