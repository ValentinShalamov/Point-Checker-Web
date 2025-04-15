package labs.pointsbackend.model.repositories;

import labs.pointsbackend.model.entities.Point;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PointRepository extends JpaRepository<Point, Long> {

    List<Point> findPointsByUserId(long userId);

    Point findPointByIdAndUserId(long id, long userId);
    
    void removePointByIdAndUserId(long id, long userId);
}
