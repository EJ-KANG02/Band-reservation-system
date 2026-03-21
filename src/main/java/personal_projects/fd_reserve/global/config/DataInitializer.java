package personal_projects.fd_reserve.global.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import personal_projects.fd_reserve.domain.Setting.entity.Setting;
import personal_projects.fd_reserve.domain.Setting.repository.SettingRepository;

@Configuration
public class DataInitializer {

    @Bean
    public CommandLineRunner initSettingData(SettingRepository settingRepository) {
        return args -> {
            if (settingRepository.findById(1L).isEmpty()) {
                Setting defaultSetting = Setting.builder()
                        .id(1L)
                        .ensembleMaxCountPerWeek(3)
                        .ensembleMaxTime(3)
                        .drumMaxCountPerWeek(3)
                        .drumMaxTime(3)
                        .build();
                settingRepository.save(defaultSetting);
                System.out.println("초기 설정 데이터 생성 완료");
            }
        };
    }
}
