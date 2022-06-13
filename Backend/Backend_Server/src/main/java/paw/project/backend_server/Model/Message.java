package paw.project.backend_server.Model;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.util.Date;

@Data
@Entity
@Table(name="message")
public class Message implements Comparable<Message> {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="message_id")
    private Integer messageId;

    @Column(name="chat_id")
    private Integer chatId;

    @Column(name="from_user")
    private Integer fromUser;

    @Column(name="text")
    private String text;

    @Column(name="file_path")
    private String filePath;

    @DateTimeFormat(pattern = "yyyy-MM-dd'T'hh:mm:ss")
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name="sent_at")
    private Date sentAt;

    @Override
    public int compareTo(Message o) {
        if(sentAt == null || o.sentAt == null)
            return 0;
        return (-1)*sentAt.compareTo(o.sentAt);
    }
}
