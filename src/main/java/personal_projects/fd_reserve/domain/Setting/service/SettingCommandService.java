package personal_projects.fd_reserve.domain.Setting.service;

import personal_projects.fd_reserve.domain.Setting.dto.SettingDTO;
import personal_projects.fd_reserve.domain.Setting.entity.Setting;

public interface SettingCommandService {
    Setting updateSetting(SettingDTO.SettingRequest.UpdateRequest request);
}
