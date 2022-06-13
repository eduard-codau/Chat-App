package paw.project.backend_server.Model;

import lombok.Data;
import org.hibernate.annotations.Generated;

import javax.persistence.*;
import java.io.Serializable;

@Embeddable
@Data
public class ChatId implements Serializable {
//    @Id
    @Column(name="chat_id")
//    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    @Column(name="user_id")
    private Integer userId;
}
