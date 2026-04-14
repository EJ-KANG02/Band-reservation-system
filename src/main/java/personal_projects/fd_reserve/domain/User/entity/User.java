package personal_projects.fd_reserve.domain.User.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import personal_projects.fd_reserve.global.common.BaseEntity;
import personal_projects.fd_reserve.global.common.enums.Role;
import personal_projects.fd_reserve.global.common.enums.Status;

@Entity
@Getter
@DynamicUpdate
@DynamicInsert
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Table(name = "users")
public class User extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String kakaoId;

    @Column(unique = true, nullable = false)
    private String nickname;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String studentId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Status status;

    private String teamName;

    public void setStatus(Status status) {
        this.status = status;
    }

    public void updateInfo(String nickname, String name, String studentId, String teamName) {
        if (nickname != null && !nickname.isBlank()) this.nickname = nickname;
        if (name != null && !name.isBlank()) this.name = name;
        if (studentId != null && !studentId.isBlank()) this.studentId = studentId;
        if (teamName != null && !teamName.isBlank()) this.teamName = teamName;
    }
}
