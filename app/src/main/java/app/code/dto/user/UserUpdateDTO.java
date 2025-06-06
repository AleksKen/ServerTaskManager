package app.code.dto.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.openapitools.jackson.nullable.JsonNullable;

@Getter
@Setter
public class UserUpdateDTO {
    private JsonNullable<String> firstName;

    private JsonNullable<String> lastName;

    private JsonNullable<String> title;

    private JsonNullable<String> role;

    @Email
    private JsonNullable<String> email;

    private JsonNullable<String> avatarProfile;

    @Size(min = 3)
    @NotBlank
    private JsonNullable<String> password;

    @NotNull
    private JsonNullable<Boolean> isAdmin;

    @NotNull
    private JsonNullable<Boolean> isActive;
}
