package paw.project.backend_server.Model;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name="user")
@Data
public class User implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer userId;

    private String password;
    private String email;

    private String username;

    private Date lastLoginDate;//represents last login date
    private Date lastLoginDateDisplay;//the actual date that we're showing

    private String role;//ex:ROLE_USER{ update perm(edit), create perm, read},ROLE_ADMIN{ delete perm,
    private String[] authorities;//permissions

    private boolean isActive;
    private boolean isNotLocked;



    public User() {}

    public User(Integer userId, String password, String email,String username, Date lastLoginDate, Date lastLoginDateDisplay, String role, String[] authorities, boolean isActive, boolean isNotLocked) {
        this.userId = userId;
        this.password = password;
        this.email = email;
        this.username = username;
        this.lastLoginDate = lastLoginDate;
        this.lastLoginDateDisplay = lastLoginDateDisplay;
        this.role = role;
        this.authorities = authorities;
        this.isActive = isActive;
        this.isNotLocked = isNotLocked;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }



    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Date getLastLoginDate() {
        return lastLoginDate;
    }

    public void setLastLoginDate(Date lastLoginDate) {
        this.lastLoginDate = lastLoginDate;
    }

    public Date getLastLoginDateDisplay() {
        return lastLoginDateDisplay;
    }

    public void setLastLoginDateDisplay(Date lastLoginDateDisplay) {
        this.lastLoginDateDisplay = lastLoginDateDisplay;
    }



    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String[] getAuthorities() {
        return authorities;
    }

    public void setAuthorities(String[] authorities) {
        this.authorities = authorities;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public boolean isNotLocked() {
        return isNotLocked;
    }

    public void setNotLocked(boolean notLocked) {
        isNotLocked = notLocked;
    }
}
