package personal_projects.fd_reserve.domain.User.service.impl;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import personal_projects.fd_reserve.domain.Officer.dto.OfficerDTO;
import personal_projects.fd_reserve.domain.Officer.entity.Officer;
import personal_projects.fd_reserve.domain.Officer.repository.OfficerRepository;
import personal_projects.fd_reserve.domain.User.converter.UserConverter;
import personal_projects.fd_reserve.domain.User.dto.UserDTO;
import personal_projects.fd_reserve.domain.User.entity.User;
import personal_projects.fd_reserve.domain.User.repository.UserRepository;
import personal_projects.fd_reserve.domain.User.service.AuthService;
import personal_projects.fd_reserve.global.common.enums.Role;
import personal_projects.fd_reserve.global.common.enums.Status;
import personal_projects.fd_reserve.global.error.code.status.ErrorStatus;
import personal_projects.fd_reserve.global.error.handler.UserException;

@Service
@Transactional
public class AuthServiceImpl implements AuthService{
    private final UserRepository userRepository;
    private final OfficerRepository officerRepository;

    public AuthServiceImpl(UserRepository userRepository, OfficerRepository officerRepository) {
        this.userRepository = userRepository;
        this.officerRepository = officerRepository;
    }

    @Override
    public  User login(String kakaoId){
        User user = userRepository.findByKakaoId(kakaoId)
                .orElseThrow(() -> new UserException(ErrorStatus.USER_NOT_FOUND));

        //승인되지 않은 회장단 계정 로그인 제한
        if (user.getStatus() == Status.PENDING) {
            throw new UserException(ErrorStatus.OFFICER_NOT_APPROVED);
        }

        return user;
    }

    public User signUp(UserDTO.UserRequest.SignUpRequest request) {
        if (userRepository.existsByKakaoId(request.getKakaoId())){
            throw new UserException(ErrorStatus.USER_ALREADY_EXISTIS);
        }

        if (userRepository.existsByNickname(request.getNickname())) {
            throw new UserException(ErrorStatus.NICKNAME_ALREADY_EXISTS);
        }

        User user = UserConverter.toUser(request);

        //승인 프로세스
        if (request.getRole() == Role.OFFICER){
            user.setStatus(Status.PENDING);
        } else {
            user.setStatus(Status.ACTIVE);
        }

        User savedUser = userRepository.save(user);

        if (request.getRole() == Role.OFFICER && request.getOfficerInfo() != null) {
            saveOfficerInfo(savedUser,request.getOfficerInfo());
        }

        return savedUser;
    }

    private void saveOfficerInfo(User user, OfficerDTO.OfficerRequest.officerSignUpRequest officerInfo){
        Officer officer = Officer.builder()
                .user(user)
                .batch(officerInfo.getBatch())
                .position(officerInfo.getPosition())
                .build();
        officerRepository.save(officer);
    }
}
