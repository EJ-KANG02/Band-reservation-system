package personal_projects.fd_reserve.domain.User.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import personal_projects.fd_reserve.domain.User.dto.UserDTO;
import personal_projects.fd_reserve.domain.User.service.userService;
import personal_projects.fd_reserve.global.error.ApiResponse;
import personal_projects.fd_reserve.global.jwt.JwtFilter;

@Tag(name = "User", description = "로그아웃 및 회원 탈퇴 API")
@RestController
@Validated
@RequiredArgsConstructor
@RequestMapping("/api/v0/user")
public class UserController {
    private final userService userService;
    private final JwtFilter jwtFilter;

    @Operation(summary = "로그아웃 API", description = "현재 로그인된 세션을 종료합니다.")
    @PostMapping("/logout")
    public ApiResponse<String> logout(HttpServletRequest request) {
        String token = jwtFilter.resolveToken(request);
        userService.logout(token);
        return ApiResponse.onSuccess("로그아웃되었습니다.");
    }

    @Operation(summary = "회원 탈퇴 API", description = "계정을 삭제하고 모든 예약 데이터를 정리합니다.")
    @DeleteMapping("/withdraw")
    public ApiResponse<String> withdraw(
            @AuthenticationPrincipal UserDetails principal,
            HttpServletRequest request
    ) {
        String token = jwtFilter.resolveToken(request); // 또는 tokenProvider.resolveToken
        userService.withdraw(principal, token);
        return ApiResponse.onSuccess("탈퇴 및 로그아웃 처리가 완료되었습니다.");
    }

    @Operation(
            summary = "내 프로필 조회 API",
            description = "현재 로그인한 사용자의 프로필 정보를 조회합니다."
    )
    @GetMapping("/info")
    public ApiResponse<UserDTO.UserResponse.UserInfoResponse> getMyInfo(
            @AuthenticationPrincipal UserDetails userDetails) {
        UserDTO.UserResponse.UserInfoResponse response =
                userService.getMyInfo(userDetails.getUsername());
        return ApiResponse.onSuccess(response);
    }

    @Operation(
            summary = "내 프로필 수정 API",
            description = "닉네임, 이름, 학번, 팀명을 수정합니다. 포함된 필드만 수정됩니다."
    )
    @PatchMapping("/info")
    public ApiResponse<UserDTO.UserResponse.UserInfoResponse> updateMyInfo(
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestBody UserDTO.UserRequest.UpdateInfoRequest request) {
        UserDTO.UserResponse.UserInfoResponse response =
                userService.updateMyInfo(userDetails.getUsername(), request);
        return ApiResponse.onSuccess(response);
    }
}
