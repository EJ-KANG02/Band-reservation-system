package personal_projects.fd_reserve.domain.Setting.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import personal_projects.fd_reserve.domain.Setting.dto.SettingDTO;
import personal_projects.fd_reserve.domain.Setting.entity.Setting;
import personal_projects.fd_reserve.domain.Setting.repository.SettingRepository;
import personal_projects.fd_reserve.domain.Setting.service.SettingCommandService;
import personal_projects.fd_reserve.global.error.code.status.ErrorStatus;
import personal_projects.fd_reserve.global.error.handler.SettingException;

@Service
@RequiredArgsConstructor
@Transactional
public class SettingCommandServiceImpl implements SettingCommandService {

    private final SettingRepository settingRepository;

    @Override
    public Setting updateSetting(SettingDTO.SettingRequest.UpdateRequest request) {
        Setting setting = settingRepository.findById(1L)
                .orElseThrow(() -> new SettingException(ErrorStatus.SETTING_NOT_FOUND));

        setting.update(request);
        return setting;
    }

}
