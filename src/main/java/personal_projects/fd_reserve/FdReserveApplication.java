package personal_projects.fd_reserve;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class FdReserveApplication {

    public static void main(String[] args) {
        SpringApplication.run(FdReserveApplication.class, args);
    }

}
