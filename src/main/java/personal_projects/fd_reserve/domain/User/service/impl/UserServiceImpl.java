package personal_projects.fd_reserve.domain.User.service.impl;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import personal_projects.fd_reserve.domain.Officer.repository.OfficerRepository;
import personal_projects.fd_reserve.domain.Reservation.entity.Reservation;
import personal_projects.fd_reserve.domain.Reservation.repository.ReservationRepository;
import personal_projects.fd_reserve.domain.User.converter.UserConverter;
import personal_projects.fd_reserve.domain.User.dto.UserDTO;
import personal_projects.fd_reserve.domain.User.entity.BlacklistToken;
import personal_projects.fd_reserve.domain.User.entity.User;
import personal_projects.fd_reserve.domain.User.repository.BlacklistRepository;
import personal_projects.fd_reserve.domain.User.repository.UserRepository;
import personal_projects.fd_reserve.domain.User.service.userService;
import personal_projects.fd_reserve.global.error.code.status.ErrorStatus;
import personal_projects.fd_reserve.global.error.handler.UserException;
import personal_projects.fd_reserve.global.jwt.TokenProvider;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class UserServiceImpl implements userService {

    private final TokenProvider tokenProvider;
    private final BlacklistRepository blacklistRepository;
    private final UserRepository userRepository;
    private final ReservationRepository reservationRepository;
    private final OfficerRepository officerRepository;

    @Override
    public void logout(String token) {
        // 토큰이 유효한지 먼저 확인 후
        if (tokenProvider.validateToken(token)) {
            BlacklistToken blacklistToken = new BlacklistToken(token, LocalDateTime.now());
            blacklistRepository.save(blacklistToken);
        }
    }

    public void withdraw(UserDetails principal, String token) {
        User user = userRepository.findByKakaoId(principal.getUsername())
                .orElseThrow(() -> new UserException(ErrorStatus.USER_NOT_FOUND));

        List<Reservation> myReservations = reservationRepository.findAllByUser(user);

        reservationRepository.deleteAllInBatch(myReservations);
        officerRepository.deleteByUser(user);

        userRepository.delete(user);
        this.logout(token);
    }

    public UserDTO.UserResponse.UserInfoResponse getMyInfo(String kakaoId) {
        User user = userRepository.findByKakaoId(kakaoId)
                .orElseThrow(() -> new UserException(ErrorStatus.USER_NOT_FOUND));

        return UserConverter.toUserInfoResponse(user);
    }

    public UserDTO.UserResponse.UserInfoResponse updateMyInfo(
            String kakaoId,
            UserDTO.UserRequest.UpdateInfoRequest request) {

        User user = userRepository.findByKakaoId(kakaoId)
                .orElseThrow(() -> new UserException(ErrorStatus.USER_NOT_FOUND));

        // 닉네임 중복 체크 (본인 닉네임 제외)
        if (request.getNickname() != null
                && !request.getNickname().isBlank()
                && !request.getNickname().equals(user.getNickname())) {
            if (userRepository.existsByNickname(request.getNickname())) {
                throw new UserException(ErrorStatus.NICKNAME_ALREADY_EXISTS);
            }
        }

        // 도메인 메서드로 수정
        user.updateInfo(
                request.getNickname(),
                request.getName(),
                request.getStudentId(),
                request.getTeamName()  // ← 추가
        );

        return UserConverter.toUserInfoResponse(user);

    }
}
