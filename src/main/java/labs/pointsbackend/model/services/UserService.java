package labs.pointsbackend.model.services;

import labs.pointsbackend.exception.UserAlreadyExistsException;
import labs.pointsbackend.exception.UserNotFoundException;
import labs.pointsbackend.model.dto.CredentialsDto;
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
    public User registerUserAndGet(CredentialsDto credentialsDto) {
        if (userRepository.findUserByName(credentialsDto.name()) == null) {
            return userRepository.save(userSecurityService.createSessionUser(credentialsDto));
        } else {
            throw new UserAlreadyExistsException(USER_ALREADY_REGISTERED);
        }
    }

    @Transactional
    public User getUserWithNewSession(CredentialsDto credentialsDto) {
        User user = userRepository.findUserByNameAndPassword(
                credentialsDto.name(), userSecurityService.encodePassword(credentialsDto.password()));

        if (user != null) {
            user.setSessionId(userSecurityService.generateSessionId());
            user.setSessionIdExpirationDate(userSecurityService.generateExpirationDate());
            return user;
        } else {
            throw new UserNotFoundException(USER_NOT_FOUND);
        }
    }

    @Transactional
    public void eraseSessionId(String sessionId) {
        Optional.of(findUserBySessionId(sessionId))
                .ifPresent(user -> user.setSessionId(null));
    }

    public User findUserBySessionId(String sessionId) {
        return userRepository.findUserBySessionId(sessionId);
    }

    public User findUserById(long userId) {
        return userRepository.findUserById(userId);
    }
}
