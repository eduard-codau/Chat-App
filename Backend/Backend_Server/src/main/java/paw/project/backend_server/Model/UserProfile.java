package paw.project.backend_server.Model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Data
@Entity
@Table(name="user_profile")
public class UserProfile implements Serializable {
    @Id
    private Integer userId;

    @Column(name="first_name")
    private String firstName;

    @Column(name="last_name")
    private String lastName;

    @Column(name="status")
    private String status;

    @Column(name="faculty")
    private String faculty;

    @Column(name="gender")
    private String gender;

    @Column(name="birthday")
    @JsonFormat(shape=JsonFormat.Shape.STRING,pattern="yyyy-MM-dd")
    private Date birthday;

    @Column(name="avatar_path")
    private String avatarPath;

    @Column(name="phone_number")
    private String phoneNumber;

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

    public Integer getUserId() {
        return userId;
    }
}
