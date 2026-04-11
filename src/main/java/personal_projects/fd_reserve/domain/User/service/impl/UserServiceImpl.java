package personal_projects.fd_reserve.domain.User.service.impl;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import personal_projects.fd_reserve.domain.Officer.repository.OfficerRepository;
import personal_projects.fd_reserve.domain.Reservation.entity.Reservation;
import personal_projects.fd_reserve.domain.Reservation.repository.ReservationRepository;
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
}
