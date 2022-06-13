package paw.project.backend_server.Model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class FriendDTO {
    private Integer friendshipId;

    private Integer friendId;

    private Integer chatId;
}
