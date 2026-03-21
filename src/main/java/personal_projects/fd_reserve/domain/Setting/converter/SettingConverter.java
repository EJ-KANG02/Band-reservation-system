package personal_projects.fd_reserve.domain.Setting.converter;

import personal_projects.fd_reserve.domain.Setting.dto.SettingDTO;
import personal_projects.fd_reserve.domain.Setting.entity.Setting;

public class SettingConverter {

    public static SettingDTO.SettingResponse.UpdateResponse toUpdateResult(Setting setting){
        return SettingDTO.SettingResponse.UpdateResponse.builder()
                .ensembleMaxCountPerWeek(setting.getEnsembleMaxCountPerWeek())
                .ensembleMaxTime(setting.getEnsembleMaxTime())
                .drumMaxCountPerWeek(setting.getDrumMaxCountPerWeek())
                .drumMaxTime(setting.getDrumMaxTime())
                .updatedAt(setting.getUpdatedAt())
                .build();
    }
}
