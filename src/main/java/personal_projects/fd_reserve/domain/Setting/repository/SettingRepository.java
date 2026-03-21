package personal_projects.fd_reserve.domain.Setting.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import personal_projects.fd_reserve.domain.Setting.entity.Setting;

public interface SettingRepository extends JpaRepository<Setting, Long> {
}
