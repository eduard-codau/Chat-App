package paw.project.backend_server.Model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import javax.persistence.Column;
import java.util.Date;

@Data
@AllArgsConstructor
@Builder
public class UserProfileDTO {
    private Integer userId;

    private String username;

    private String email;

    private String firstName;

    private String lastName;

    private String status;

    private String faculty;

    private String gender;

    @JsonFormat(shape=JsonFormat.Shape.STRING,pattern="yyyy-MM-dd")
    private Date birthday;

    private String avatarPath;

    private String phoneNumber;

    private Integer friendsCount;

    public String getFaculty()
    {
        return this.faculty;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }
}
