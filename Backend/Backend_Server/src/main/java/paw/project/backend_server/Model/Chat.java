package paw.project.backend_server.Model;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name="chat")
public class Chat {
    @EmbeddedId
//    @GeneratedValue(strategy = GenerationType.AUTO)
    private ChatId chatId;
}
