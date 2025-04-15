package labs.pointsbackend.model.services;

import labs.pointsbackend.model.dto.PointDto;
import labs.pointsbackend.model.entities.Point;
import labs.pointsbackend.model.entities.User;
import labs.pointsbackend.model.repositories.PointRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@AllArgsConstructor
public class PointService {
    private PointRepository pointRepository;
    private UserService userService;
    private AreaCheckerService areaChecker;

    public List<Point> findPointsByUserId(long userId) {
        return pointRepository.findPointsByUserId(userId);
    }

    @Transactional
    public void addPoint(PointDto pointDto, long userId) {
        Point point = toPoint(pointDto);
        User user = userService.findUserById(userId);
        point.setUser(user);
        pointRepository.save(point);
    }

    @Transactional
    public void updatePoint(PointDto pointDto, long userId) {
        Point newPoint = toPoint(pointDto);

        Point oldPoint = pointRepository.findPointByIdAndUserId(pointDto.id(), userId);
        if (oldPoint != null) {
            oldPoint.setX(newPoint.getX());
            oldPoint.setY(newPoint.getY());
            oldPoint.setRadius(newPoint.getRadius());
            oldPoint.setResult(newPoint.isResult());
        }
    }

    @Transactional
    public void deletePoint(PointDto pointDto, long userId) {
        pointRepository.removePointByIdAndUserId(pointDto.id(), userId);
    }

    private Point toPoint(PointDto pointDto) {
        Point point = new Point(pointDto.x(), pointDto.y(), pointDto.r());
        point.setResult(areaChecker.getResultForPoint(point));
        return point;
    }

}
