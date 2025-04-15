package labs.pointsbackend.controllers;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import labs.pointsbackend.model.dto.UserDto;
import labs.pointsbackend.model.entities.User;
import labs.pointsbackend.model.services.UserService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@AllArgsConstructor
public class LoginController {
    private static final String ERROR_MESSAGE = "errorMessage";
    private static final String USERNAME = "username";

    private UserService userService;

    @PostMapping("/login")
    public Map<String, String> login(
            @Valid @ModelAttribute UserDto userDto,
            BindingResult bindingResult,
            HttpServletResponse response
    ) {
        Map<String, String> validationMessageMap = new HashMap<>();
        if (bindingResult.hasErrors()) {
            for (FieldError fieldError : bindingResult.getFieldErrors()) {
                validationMessageMap.put(fieldError.getField(), fieldError.getDefaultMessage());
            }
            return validationMessageMap;
        }

        if (userDto.isRegistration()) {
            User user = userService.registerUserAndGet(userDto);
            if (user == null) {
                return Map.of(ERROR_MESSAGE, UserService.USER_ALREADY_REGISTERED);
            } else {
                response.addHeader(HttpHeaders.AUTHORIZATION, user.getSessionId());
                return Map.of(USERNAME, user.getName());
            }
        } else {
            User user = userService.getUserWithNewSession(userDto);
            if (user == null) {
                return Map.of(ERROR_MESSAGE, UserService.USER_NOT_FOUND);
            } else {
                response.addHeader(HttpHeaders.AUTHORIZATION, user.getSessionId());
                return Map.of(USERNAME, user.getName());
            }
        }
    }
}
