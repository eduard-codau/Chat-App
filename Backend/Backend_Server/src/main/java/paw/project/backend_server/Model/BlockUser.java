package paw.project.backend_server.Model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Table(name="block_user")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class BlockUser {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="block_request_id")
    private Integer requestId;

    @Column(name="by_user")
    private Integer byUserId;

    @Column(name="to_user")
    private Integer toUserId;

    public BlockUser(Integer byUserId, Integer toUserId)
    {
        this.byUserId = byUserId;
        this.toUserId = toUserId;
    }
}
