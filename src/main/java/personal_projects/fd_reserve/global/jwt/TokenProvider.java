package personal_projects.fd_reserve.global.jwt;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.antlr.v4.runtime.Token;
import org.springframework.stereotype.Component;
import personal_projects.fd_reserve.domain.User.entity.User;
import personal_projects.fd_reserve.global.jwt.dto.TokenDTO;

import java.security.Key;
import java.util.Date;

@Component
public class TokenProvider {
    private final Key key = Keys.secretKeyFor(SignatureAlgorithm.HS256);
    private final long tokenValidityInMilliseconds = 1000L * 60 * 60 * 24; //유효 기간 24시간

    public TokenDTO createToken(User user){

        long now = (new Date()).getTime();
        long validityTime = now + this.tokenValidityInMilliseconds;
        Date validity = new Date(validityTime);

        String accesesToken = Jwts.builder()
                .setSubject(user.getKakaoId())
                .claim("role", user.getRole().name())
                .signWith(key, SignatureAlgorithm.HS256)
                .setExpiration(validity)
                .compact();

        return TokenDTO.builder()
                .grantType("Bearer")
                .accessToken(accesesToken)
                .accessTokenExpiresIn(validityTime)
                .build();
    }

    public boolean validateToken(String token){
        try {
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
            return true;
        } catch (Exception e){
            return false;
        }
    }
}
