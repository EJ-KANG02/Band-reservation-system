package personal_projects.fd_reserve.domain.User.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import personal_projects.fd_reserve.domain.User.entity.BlacklistToken;

public interface BlacklistRepository extends JpaRepository<BlacklistToken, String> {
}
