package labs.pointsbackend.model.dto;

import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import org.springframework.web.bind.annotation.BindParam;

public record PointDto(
        @BindParam("id")
        Long id,

        @BindParam("x_value")
        @NotNull
        @Min(value = MIN_X_VALUE)
        @Max(value = MAX_X_VALUE)
        Double x,

        @BindParam("y_value")
        @NotNull
        @Min(value = MIN_Y_VALUE)
        @Max(value = MAX_Y_VALUE)
        Double y,

        @BindParam("r_value")
        @NotNull
        @Min(value = MIN_R_VALUE)
        @Max(value = MAX_R_VALUE)
        @Digits(integer = 1, fraction = 0)
        Integer r)
{
    private static final int MIN_X_VALUE = -8;
    private static final int MAX_X_VALUE = 8;
    private static final int MIN_Y_VALUE = -8;
    private static final int MAX_Y_VALUE = 8;
    private static final int MIN_R_VALUE = 1;
    private static final int MAX_R_VALUE = 5;

}

