package personal_projects.fd_reserve.domain.Setting.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import personal_projects.fd_reserve.domain.Setting.converter.SettingConverter;
import personal_projects.fd_reserve.domain.Setting.dto.SettingDTO;
import personal_projects.fd_reserve.domain.Setting.entity.Setting;
import personal_projects.fd_reserve.domain.Setting.service.SettingCommandService;
import personal_projects.fd_reserve.global.error.ApiResponse;

@Tag(name = "Setting", description = "시스템 설정 관련 API (회장단 전용)")
@RestController
@RequestMapping("/api/v0/settings")
@RequiredArgsConstructor
public class SettingController {

    private final SettingCommandService settingCommandService;

    @Operation(
            summary = "연습실 이용 규칙 수정 API",
            description = "주당 최대 예약 횟수, 회당 이용 시간 등 연습실 운영 규칙을 수정합니다. " +
                    "현재 시스템은 단일 설정(ID: 1)을 공유하므로 경로 변수는 1로 고정하여 호출합니다."
    )
    @PatchMapping("/{settingId}")
    public ApiResponse<SettingDTO.SettingResponse.UpdateResponse> updateSetting(
            @Parameter(description = "수정할 설정 ID (현재는 1번 고정)", example = "1")
            @PathVariable(name = "settingId") Long settingId,
            @RequestBody @Valid SettingDTO.SettingRequest.UpdateRequest request
    ){
        Setting setting = settingCommandService.updateSetting(request);
        return ApiResponse.onSuccess(SettingConverter.toUpdateResult(setting));
    }
}
