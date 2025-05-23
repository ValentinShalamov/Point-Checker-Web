package labs.pointsbackend.controllers;

import jakarta.validation.Valid;
import labs.pointsbackend.config.security.authentications.UserPrincipal;
import labs.pointsbackend.model.dto.PointDto;
import labs.pointsbackend.model.entities.Point;
import labs.pointsbackend.model.services.PointService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/point")
@AllArgsConstructor
public class PointController {

    private PointService pointService;

    @GetMapping
    public List<Point> getPoints(@AuthenticationPrincipal UserPrincipal userPrincipal) {
        return pointService.findPointsByUserId(userPrincipal.getUserId());
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void addPoint(@RequestBody @Valid PointDto pointDto,
                         @AuthenticationPrincipal UserPrincipal userPrincipal) {
        pointService.addPoint(pointDto, userPrincipal.getUserId());
    }

    @PutMapping
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void updatePoint(@RequestBody @Valid PointDto pointDto,
                            @AuthenticationPrincipal UserPrincipal userPrincipal) {
        pointService.updatePoint(pointDto, userPrincipal.getUserId());
    }

    @DeleteMapping
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deletePoint(@RequestBody PointDto pointDto,
                            @AuthenticationPrincipal UserPrincipal userPrincipal) {
        pointService.deletePoint(pointDto, userPrincipal.getUserId());
    }

}
