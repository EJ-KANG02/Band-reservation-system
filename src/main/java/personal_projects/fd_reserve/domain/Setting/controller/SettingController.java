package personal_projects.fd_reserve.domain.Setting.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import personal_projects.fd_reserve.domain.Setting.converter.SettingConverter;
import personal_projects.fd_reserve.domain.Setting.dto.SettingDTO;
import personal_projects.fd_reserve.domain.Setting.entity.Setting;
import personal_projects.fd_reserve.domain.Setting.service.SettingCommandService;
import personal_projects.fd_reserve.global.error.ApiResponse;

@RestController
@RequestMapping("/api/v0/settings")
@RequiredArgsConstructor
public class SettingController {

    private final SettingCommandService settingCommandService;

    @PatchMapping("/1")
    public ApiResponse<SettingDTO.SettingResponse.UpdateResponse> updateSetting(
            @RequestBody @Valid SettingDTO.SettingRequest.UpdateRequest request
    ){
        Setting setting = settingCommandService.updateSetting(request);
        return ApiResponse.onSuccess(SettingConverter.toUpdateResult(setting));
    }
}
