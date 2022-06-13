package paw.project.backend_server.Model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Table(name="friend_request")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class FriendRequest {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="friend_request_id")
    private Integer requestId;

    @Column(name="from_user")
    private Integer senderUserId;

    @Column(name="to_user")
    private Integer receiverUserId;

    public FriendRequest(Integer senderUserId, Integer receiverUserId)
    {
        this.senderUserId = senderUserId;
        this.receiverUserId = receiverUserId;
    }

}
