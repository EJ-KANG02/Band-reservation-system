package personal_projects.fd_reserve.domain.Setting.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.*;
import personal_projects.fd_reserve.domain.Setting.dto.SettingDTO;
import personal_projects.fd_reserve.global.common.BaseEntity;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class Setting extends BaseEntity {
    @Id
    private Long id;

    //합주 설정
    private Integer ensembleMaxCountPerWeek; //주당 횟수
    private Integer ensembleMaxTime; //회당 시간

    //드럼 설정
    private Integer drumMaxCountPerWeek;
    private Integer drumMaxTime;

    public void update(SettingDTO.SettingRequest.UpdateRequest request) {
        this.ensembleMaxCountPerWeek = request.getEnsembleMaxCountPerWeek();
        this.ensembleMaxTime = request.getEnsembleMaxTime();
        this.drumMaxCountPerWeek = request.getDrumMaxCountPerWeek();
        this.drumMaxTime = request.getDrumMaxTime();
    }
}
