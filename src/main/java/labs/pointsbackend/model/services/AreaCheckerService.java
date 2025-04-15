package labs.pointsbackend.model.services;

import labs.pointsbackend.model.entities.Point;
import org.springframework.stereotype.Service;

@Service
public class AreaCheckerService {
    private static final boolean HIT = true;
    private static final boolean MISS = false;

    public boolean getResultForPoint(Point point) {
        if (doesRectangleContainPoint(point)
                || doesTriangleContainPoint(point)
                || doesQuarterCircleContainPoint(point)) {
            return HIT;
        } else {
            return MISS;
        }
    }

    private boolean doesRectangleContainPoint(Point point) {
        return point.getX() >= -point.getRadius() && point.getX() <= 0
                && point.getY() >= 0 && point.getY() <= point.getRadius() / 2.0;
    }

    private boolean doesTriangleContainPoint(Point point) {
        if (point.getY() < -point.getRadius() / 2.0 || point.getY() > 0
                || point.getX() < -point.getRadius() || point.getX() > 0) {
            return false;
        }
        double minY = -0.5 * point.getX() - point.getRadius() / 2.0;

        return point.getY() >= minY;
    }

    private boolean doesQuarterCircleContainPoint(Point point) {
        if (point.getX() < 0 || point.getX() > point.getRadius() / 2.0
                || point.getY() > 0 || point.getY() < -point.getRadius() / 2.0) {
            return false;
        }
        return Math.pow(point.getX(), 2) + Math.pow(point.getY(), 2) <= Math.pow(point.getRadius() / 2.0, 2);
    }
}
