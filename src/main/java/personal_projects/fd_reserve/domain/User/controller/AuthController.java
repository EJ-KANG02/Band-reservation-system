package personal_projects.fd_reserve.domain.User.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import personal_projects.fd_reserve.domain.User.dto.UserDTO;
import personal_projects.fd_reserve.domain.User.entity.User;
import personal_projects.fd_reserve.domain.User.repository.UserRepository;
import personal_projects.fd_reserve.domain.User.service.AuthService;
import personal_projects.fd_reserve.domain.User.service.impl.AuthServiceImpl;
import personal_projects.fd_reserve.global.error.ApiResponse;
import personal_projects.fd_reserve.global.jwt.dto.TokenDTO;

@RestController
@Validated
@RequiredArgsConstructor
@RequestMapping("/api/v0/auth")
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    public ApiResponse<TokenDTO> login(@Valid @RequestBody UserDTO.UserRequest.LoginRequest request) {
        // DB에 있으면 유저 객체(학번, 팀 등 포함) 반환, 없으면 404 예외 발생
        TokenDTO tokenDTO = authService.login(request.getKakaoId());
        return ApiResponse.onSuccess(tokenDTO);
    }

    @PostMapping("/signup")
    public ApiResponse<TokenDTO> signUp(@Valid @RequestBody UserDTO.UserRequest.SignUpRequest request) {
        // 모든 정보(이름, 학번, 팀, 역할 등)를 받아서 저장
        TokenDTO tokenDTO = authService.signUp(request);
        return ApiResponse.onSuccess(tokenDTO);
    }

}
