package labs.pointsbackend.model.services;

import labs.pointsbackend.model.entities.User;
import labs.pointsbackend.model.dto.UserDto;
import lombok.AllArgsConstructor;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@AllArgsConstructor
public class UserSecurityService {
    private static final int EXPIRY_DATE_SESSION_IN_HOURS = 24;

    public String generateSessionId() {
       return UUID.randomUUID().toString();
    }

    public String encodePassword(String password) {
        return DigestUtils.sha256Hex(password);
    }

    public LocalDateTime generateExpirationDate() {
        return LocalDateTime.now().plusHours(EXPIRY_DATE_SESSION_IN_HOURS);
    }

    public User createSessionUser(UserDto userDto) {
        return new User(
                userDto.name(),
                encodePassword(userDto.password()),
                generateSessionId(),
                generateExpirationDate());
    }
}
