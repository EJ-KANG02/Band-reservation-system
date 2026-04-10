package personal_projects.fd_reserve.domain.User.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import personal_projects.fd_reserve.global.common.BaseEntity;

import java.time.LocalDateTime;

@Entity
@Getter
@DynamicUpdate
@DynamicInsert
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Table(name = "blacklist")
public class BlacklistToken extends BaseEntity {
    @Id
    private String token;
    private LocalDateTime logoutTime;
}
