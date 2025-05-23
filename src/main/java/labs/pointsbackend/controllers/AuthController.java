package labs.pointsbackend.controllers;

import jakarta.validation.Valid;
import labs.pointsbackend.model.dto.AuthResultDto;
import labs.pointsbackend.model.dto.CredentialsDto;
import labs.pointsbackend.model.entities.User;
import labs.pointsbackend.model.services.UserService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
public class AuthController {
    private UserService userService;

    @PostMapping("/login")
    public ResponseEntity<AuthResultDto> login(
            @Valid @RequestBody CredentialsDto credentialsDto) {
        return createAuthResponse(userService.getUserWithNewSession(credentialsDto));
    }

    @PostMapping("/register")
    public ResponseEntity<AuthResultDto> register(
            @Valid @RequestBody CredentialsDto credentialsDto) {
        return createAuthResponse(userService.registerUserAndGet(credentialsDto));
    }

    private ResponseEntity<AuthResultDto> createAuthResponse(User user) {
        return ResponseEntity.ok()
                .header(HttpHeaders.AUTHORIZATION, user.getSessionId())
                .body(new AuthResultDto(null, user.getName()));
    }

}
