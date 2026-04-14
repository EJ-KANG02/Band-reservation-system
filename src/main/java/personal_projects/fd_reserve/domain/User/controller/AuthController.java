package personal_projects.fd_reserve.domain.User.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import personal_projects.fd_reserve.domain.User.dto.UserDTO;
import personal_projects.fd_reserve.domain.User.entity.User;
import personal_projects.fd_reserve.domain.User.repository.UserRepository;
import personal_projects.fd_reserve.domain.User.service.AuthService;
import personal_projects.fd_reserve.domain.User.service.impl.AuthServiceImpl;
import personal_projects.fd_reserve.global.error.ApiResponse;
import personal_projects.fd_reserve.global.jwt.dto.TokenDTO;

@Tag(name = "Auth", description = "인증 및 회원가입 관련 API")
@RestController
@Validated
@RequiredArgsConstructor
@RequestMapping("/api/v0/auth")
public class AuthController {

    private final AuthService authService;

    @Operation(
            summary = "카카오 로그인 API",
            description = "카카오 고유 ID를 통해 로그인을 시도합니다. " +
                    "DB에 등록된 사용자인 경우 액세스 토큰을 반환하며, 등록되지 않은 경우 404(USER_NOT_FOUND) 에러를 반환하여 회원가입으로 유도합니다."
    )
    @PostMapping("/login")
    public ApiResponse<TokenDTO> login(@Valid @RequestBody UserDTO.UserRequest.LoginRequest request) {
        // DB에 있으면 유저 객체(학번, 팀 등 포함) 반환, 없으면 404 예외 발생
        TokenDTO tokenDTO = authService.login(request.getKakaoId());
        return ApiResponse.onSuccess(tokenDTO);
    }

    @Operation(
            summary = "서비스 회원가입 API",
            description = "카카오 ID와 함께 이름, 학번, 팀명, 역할 등 사용자 정보를 등록하고 액세스 토큰을 발급받습니다."
    )
    @PostMapping("/signup")
    public ApiResponse<TokenDTO> signUp(@Valid @RequestBody UserDTO.UserRequest.SignUpRequest request) {
        // 모든 정보(이름, 학번, 팀, 역할 등)를 받아서 저장
        TokenDTO tokenDTO = authService.signUp(request);
        return ApiResponse.onSuccess(tokenDTO);
    }

    @Operation(
            summary = "닉네임 중복 확인 API",
            description = "회원가입 시 닉네임 중복 여부를 확인합니다. 사용 가능하면 true, 중복이면 false를 반환합니다."
    )
    @GetMapping("/check-nickname")
    public ApiResponse<Boolean> checkNickname(
            @RequestParam @NotBlank String nickname) {
        boolean isAvailable = authService.checkNickname(nickname);
        return ApiResponse.onSuccess(isAvailable);
    }
}
