package paw.project.backend_server.Model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class PairOfUsers {
    private Integer userId1;

    private Integer userId2;
}
