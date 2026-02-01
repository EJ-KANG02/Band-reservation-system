package personal_projects.fd_reserve.domain.User.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import personal_projects.fd_reserve.domain.User.dto.UserDTO;
import personal_projects.fd_reserve.domain.User.entity.User;
import personal_projects.fd_reserve.domain.User.repository.UserRepository;
import personal_projects.fd_reserve.global.jwt.dto.TokenDTO;


public interface AuthService {

    TokenDTO login(String kakaoId);
    TokenDTO signUp(UserDTO.UserRequest.SignUpRequest request);
}
