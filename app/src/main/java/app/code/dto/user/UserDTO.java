package app.code.dto.user;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

@Getter
@Setter
public class UserDTO {
    private Long id;

    private String email;

    private String firstName;

    private String lastName;

    private String title;

    private String role;

    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Instant createdAt;

    private Boolean isAdmin;

    private Boolean isActive;
}
