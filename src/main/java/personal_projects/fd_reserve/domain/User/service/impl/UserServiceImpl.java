package personal_projects.fd_reserve.domain.User.service.impl;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import personal_projects.fd_reserve.domain.User.entity.BlacklistToken;
import personal_projects.fd_reserve.domain.User.repository.BlacklistRepository;
import personal_projects.fd_reserve.domain.User.service.userService;
import personal_projects.fd_reserve.global.jwt.TokenProvider;

import java.time.LocalDateTime;

@Service
@Transactional
@RequiredArgsConstructor
public class UserServiceImpl implements userService {

    private final TokenProvider tokenProvider;
    private final BlacklistRepository blacklistRepository;

    @Override
    public void logout(String token) {
        // 토큰이 유효한지 먼저 확인 후
        if (tokenProvider.validateToken(token)) {
            BlacklistToken blacklistToken = new BlacklistToken(token, LocalDateTime.now());
            blacklistRepository.save(blacklistToken);
        }
    }
}
