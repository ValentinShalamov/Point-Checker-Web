package labs.pointsbackend.model.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import org.springframework.web.bind.annotation.BindParam;

public record UserDto (
        @BindParam("name")
        @NotNull
        @Pattern(regexp = "^([A-Za-z0-9]{3,30})$", message = NAME_VALIDATION_MESSAGE)
        String name,

        @BindParam("password")
        @NotNull
        @Pattern(regexp = "^([A-Za-z0-9]{4,30})$", message = PASSWORD_VALIDATION_MESSAGE)
        String password,

        @BindParam("isRegistration")
        @NotNull
        Boolean isRegistration)
{
    private static final int MIN_LENGTH_NAME = 3;
    private static final int MAX_LENGTH_NAME = 30;

    private static final int MIN_LENGTH_PASSWORD = 4;
    private static final int MAX_LENGTH_PASSWORD = 30;

    private static final String NAME_VALIDATION_MESSAGE =
            "The length of the name must be between " + MIN_LENGTH_NAME + " and " + MAX_LENGTH_NAME +
                    ". Only allowed Latin letter and digits with no special characters";
    private static final String PASSWORD_VALIDATION_MESSAGE =
            "The length of the password must be between " + MIN_LENGTH_PASSWORD + " and " + MAX_LENGTH_PASSWORD +
            ". Only allowed Latin letter and digits with no special characters";
}
