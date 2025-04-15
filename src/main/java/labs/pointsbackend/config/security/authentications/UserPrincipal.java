package labs.pointsbackend.config.security.authentications;

import labs.pointsbackend.model.entities.User;
import lombok.AllArgsConstructor;
import org.springframework.security.core.CredentialsContainer;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

@AllArgsConstructor
public class UserPrincipal implements UserDetails, CredentialsContainer {
    private User user;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of();
    }

    @Override
    public String getUsername() {
        return user != null ? user.getName() : null;
    }

    @Override
    public String getPassword() {
        return null;
    }

    @Override
    public void eraseCredentials() {
        if (user != null) {
            user.setSessionId(null);
        }
    }

    public Long getUserId() {
        return user != null ? user.getId() : null;
    }

    public LocalDateTime getExpirationDate() {
        return user != null ? user.getSessionIdExpirationDate() : null;
    }
}
