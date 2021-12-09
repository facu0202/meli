package com.facuferro.meetup.api;

import com.facuferro.meetup.domain.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Data
@Builder
@AllArgsConstructor
public class UserRequest {


    @NotBlank(message = "User name is mandatory")
    private String name;
    @NotBlank(message = "User surname is mandatory")
    private String surname;
    @NotBlank(message = "User email is mandatory")
    @Email(message = "Email invalid")
    private String email;
    @NotBlank(message = "User pass is mandatory")
    private String password;
    private Role role;

    @Override
    public String toString() {
        return "UserRequest{" +
                "name='" + name + '\'' +
                ", surname='" + surname + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", role=" + role +
                '}';
    }
}
