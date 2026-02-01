package personal_projects.fd_reserve.domain.User.converter;

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
}
