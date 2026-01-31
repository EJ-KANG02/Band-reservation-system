package personal_projects.fd_reserve.domain.User.repository;
import org.springframework.data.jpa.repository.JpaRepository;
import personal_projects.fd_reserve.domain.User.entity.User;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByKakaoId(String kakaoId);
}
