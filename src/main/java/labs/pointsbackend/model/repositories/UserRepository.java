package labs.pointsbackend.model.repositories;

import labs.pointsbackend.model.entities.User;
import lombok.NonNull;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {

    User findUserByNameAndPassword(@NonNull String name, @NonNull String password);

    User findUserBySessionId(String sessionId);

    User findUserById(long id);

    User findUserByName(String name);
}
