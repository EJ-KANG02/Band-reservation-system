package personal_projects.fd_reserve.domain.User.repository;
import jakarta.validation.constraints.NotBlank;
import org.springframework.data.jpa.repository.JpaRepository;
import personal_projects.fd_reserve.domain.User.entity.User;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByKakaoId(String kakaoId);

    boolean existsByKakaoId(@NotBlank(message = "카카오 ID는 필수 입력 값입니다.") String kakaoId);

    boolean existsByNickname(@NotBlank(message = "닉네임은 필수 입력 값입니다.") String nickname);
}
