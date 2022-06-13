package paw.project.backend_server.Model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@Entity
@Table(name="friend")
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Friend {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer friendshipId;

    @Column(name="user1")
    private Integer user1Id;

    @Column(name="user2")
    private Integer user2Id;
}
