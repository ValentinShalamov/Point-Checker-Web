package labs.pointsbackend.model.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@NoArgsConstructor
@Getter @Setter
@Entity
@Table(name = "users")
public class User {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Column(nullable = false)
    private String name;
    @Column(nullable = false)
    private String password;
    private String sessionId;
    private LocalDateTime sessionIdExpirationDate;

    public User(String name, String password, String sessionId, LocalDateTime sessionIdExpirationDate) {
        this.name = name;
        this.password = password;
        this.sessionId = sessionId;
        this.sessionIdExpirationDate = sessionIdExpirationDate;
    }
}
