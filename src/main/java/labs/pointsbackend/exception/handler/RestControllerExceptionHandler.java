package labs.pointsbackend.exception.handler;

import labs.pointsbackend.exception.UserAlreadyExistsException;
import labs.pointsbackend.exception.UserNotFoundException;
import labs.pointsbackend.model.dto.AuthResultDto;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class RestControllerExceptionHandler {
    private static final String ERROR_MESSAGE = "errorMessage";

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public AuthResultDto handleArgumentNotValidException(BindingResult bindingResult) {
        Map<String, String> errors = new HashMap<>();
        for (FieldError fieldError : bindingResult.getFieldErrors()) {
            errors.put(fieldError.getField(), fieldError.getDefaultMessage());
        }
        return new AuthResultDto(errors, null);
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(UserNotFoundException.class)
    public AuthResultDto handleUserNotFoundException(UserNotFoundException ex) {
        return new AuthResultDto(Map.of(ERROR_MESSAGE, ex.getMessage()), null);
    }

    @ResponseStatus(HttpStatus.CONFLICT)
    @ExceptionHandler(UserAlreadyExistsException.class)
    public AuthResultDto handleUserAlreadyExists(UserAlreadyExistsException ex) {
        return new AuthResultDto(Map.of(ERROR_MESSAGE, ex.getMessage()), null);
    }

}
