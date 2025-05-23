package labs.pointsbackend.model.dto;

import java.util.Map;

public record AuthResultDto(
        Map<String, String> errorMessages,
        String authUsername
) { }
