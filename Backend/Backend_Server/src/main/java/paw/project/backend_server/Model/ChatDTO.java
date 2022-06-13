package paw.project.backend_server.Model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ChatDTO {
    private Integer chatId;
    private Integer userId;
    private String username;
    private String firstName;
    private String lastName;
}
