package personal_projects.fd_reserve.domain.Officer.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import personal_projects.fd_reserve.domain.User.entity.User;
import personal_projects.fd_reserve.global.common.enums.Position;
import personal_projects.fd_reserve.global.common.enums.Role;

@Entity
@Getter
@DynamicUpdate
@DynamicInsert
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Table(name = "officers")
public class Officer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;

    @Column(nullable = false)
    private Integer batch;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Position position;
}
