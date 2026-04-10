package personal_projects.fd_reserve.domain.User.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
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
}
