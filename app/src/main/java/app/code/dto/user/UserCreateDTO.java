package app.code.dto.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserCreateDTO {
    private String firstName;

    private String lastName;

    private String title;

    private String role;

    @Email
    private String email;

    @Size(min = 3)
    @NotBlank
    private String password;

    private Boolean isAdmin;

    private Boolean isActive;
}
