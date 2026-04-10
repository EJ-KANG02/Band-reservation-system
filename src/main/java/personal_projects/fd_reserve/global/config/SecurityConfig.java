package personal_projects.fd_reserve.global.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import personal_projects.fd_reserve.domain.User.repository.BlacklistRepository;
import personal_projects.fd_reserve.global.jwt.JwtFilter;
import personal_projects.fd_reserve.global.jwt.TokenProvider;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final TokenProvider tokenProvider;
    private final BlacklistRepository blacklistRepository;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))

                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api/v0/auth/signup", "/api/v0/auth/login").permitAll() // 가입/로그인은 누구나 가능
                        .requestMatchers("/api/v0/admin/**").hasRole("OFFICER") // 어드민은 회장단만 가능
                        .requestMatchers("/api/v0/settings/**").hasRole("OFFICER") // 설정 접근은 회장단만 가능
                        .requestMatchers(
                                "/v3/api-docs/**",
                                "/swagger-ui/**").permitAll()
                        .anyRequest().authenticated() // 그 외 모든 요청은 인증 필요
                )

                .addFilterBefore(new JwtFilter(tokenProvider, blacklistRepository), UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}
