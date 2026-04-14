package personal_projects.fd_reserve.domain.User.service;

import org.springframework.security.core.userdetails.UserDetails;
import personal_projects.fd_reserve.domain.User.dto.UserDTO;

public interface userService {
    void logout(String token);
    void withdraw(UserDetails principal, String token);
    UserDTO.UserResponse.UserInfoResponse getMyInfo(String kakaoId);
    UserDTO.UserResponse.UserInfoResponse updateMyInfo(String kakaoId, UserDTO.UserRequest.UpdateInfoRequest request);
}
