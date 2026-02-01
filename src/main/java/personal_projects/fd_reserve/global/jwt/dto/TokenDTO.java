package personal_projects.fd_reserve.global.jwt.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Date;

@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class TokenDTO {
    private String grantType;
    private String accessToken; // 실제 jwt 토큰
    private long accessTokenExpiresIn; // 만료 시간
}
