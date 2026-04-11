package personal_projects.fd_reserve.domain.User.service;

import org.springframework.security.core.userdetails.UserDetails;

public interface userService {
    void logout(String token);
    void withdraw(UserDetails principal, String token);
}
