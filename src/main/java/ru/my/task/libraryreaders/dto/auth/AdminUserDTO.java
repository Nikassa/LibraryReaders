package ru.my.task.libraryreaders.dto.auth;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import ru.my.task.libraryreaders.model.auth.Status;
import ru.my.task.libraryreaders.model.auth.User;

@JsonIgnoreProperties(ignoreUnknown = true)
public class AdminUserDTO {

    private Long id;
    private String username;
    private String firstName;
    private String lastName;
    private String email;
    private String status;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public User toUser() {
        User user = new User();
        user.setId(id);
        user.setUsername(username);
        user.setFirstname(firstName);
        user.setLastname(lastName);
        user.setEmail(email);
        user.setStatus(Status.valueOf(status));
        return user;
    }

    public static AdminUserDTO fromUser(User user) {
        AdminUserDTO adminUserDTO = new AdminUserDTO();
        adminUserDTO.setId(user.getId());
        adminUserDTO.setUsername(user.getUsername());
        adminUserDTO.setFirstName(user.getFirstname());
        adminUserDTO.setLastName(user.getLastname());
        adminUserDTO.setEmail(user.getEmail());
        adminUserDTO.setStatus(user.getStatus().name());
        return adminUserDTO;
    }
}