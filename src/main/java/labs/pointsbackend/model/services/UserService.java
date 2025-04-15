package labs.pointsbackend.model.services;

import labs.pointsbackend.model.dto.UserDto;
import labs.pointsbackend.model.entities.User;
import labs.pointsbackend.model.repositories.UserRepository;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Getter
@AllArgsConstructor
public class UserService {
    private UserRepository userRepository;
    private UserSecurityService userSecurityService;

    public static final String USER_ALREADY_REGISTERED = "User already registered";
    public static final String USER_NOT_FOUND = "Such user was not found";

    @Transactional
    public User registerUserAndGet(UserDto userDto) {
        return userRepository.findUserByName(userDto.name()) == null
                ? userRepository.save(userSecurityService.createSessionUser(userDto)) : null;
    }

    @Transactional
    public User getUserWithNewSession(UserDto userDto) {

        User user = userRepository.findUserByNameAndPassword(
                userDto.name(), userSecurityService.encodePassword(userDto.password()));

        if (user != null) {
            user.setSessionId(userSecurityService.generateSessionId());
            user.setSessionIdExpirationDate(userSecurityService.generateExpirationDate());
            return user;
        } else {
            return null;
        }

    }

    @Transactional
    public void eraseSessionId(String sessionId) {
        Optional.of(findUserBySessionId(sessionId))
                .ifPresent(user -> user.setSessionId(""));
    }

    public User findUserBySessionId(String sessionId) {
        return userRepository.findUserBySessionId(sessionId);
    }

    public User findUserById(long userId) {
        return userRepository.findUserById(userId);
    }
}
