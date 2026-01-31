package personal_projects.fd_reserve.domain.User.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import personal_projects.fd_reserve.domain.User.dto.UserDTO;
import personal_projects.fd_reserve.domain.User.entity.User;
import personal_projects.fd_reserve.domain.User.repository.UserRepository;


public interface AuthService {

    User login(String kakaoId);
    User signUp(UserDTO.UserRequest.SignUpRequest request);
}
