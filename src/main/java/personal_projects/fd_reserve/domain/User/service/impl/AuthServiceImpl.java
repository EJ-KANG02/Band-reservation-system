package personal_projects.fd_reserve.domain.User.service.impl;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import personal_projects.fd_reserve.domain.User.dto.UserDTO;
import personal_projects.fd_reserve.domain.User.entity.User;
import personal_projects.fd_reserve.domain.User.repository.UserRepository;
import personal_projects.fd_reserve.domain.User.service.AuthService;
import personal_projects.fd_reserve.global.error.code.status.ErrorStatus;
import personal_projects.fd_reserve.global.error.handler.UserException;

@Service
@Transactional
public class AuthServiceImpl implements AuthService{
    private final UserRepository userRepository;

    public AuthServiceImpl(UserRepository userRepository){
        this.userRepository = userRepository;
    }

    @Override
    public  User login(String kakaoId){
        return userRepository.findByKakaoId(kakaoId)
                .orElseThrow(() -> new UserException(ErrorStatus.USER_NOT_FOUND));
    }

    public User signUp(UserDTO.UserRequest.SignUpRequest request) {
        User newUser = User.builder()
                .kakaoId(request.getKakaoId())
                .name(request.getName())
                .nickname(request.getNickname())
                .studentId(request.getStudentId())
                .teamName(request.getTeamName())
                .role(request.getRole()) // "MEMBER" 또는 "OFFICER" 구분 저장
                .build();
        return userRepository.save(newUser);
    }
}
