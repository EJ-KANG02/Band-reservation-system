package personal_projects.fd_reserve.domain.Officer.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import personal_projects.fd_reserve.domain.Officer.entity.Officer;
import personal_projects.fd_reserve.domain.User.entity.User;

public interface OfficerRepository extends JpaRepository<Officer, Long> {
    void deleteByUser(User user);
}
