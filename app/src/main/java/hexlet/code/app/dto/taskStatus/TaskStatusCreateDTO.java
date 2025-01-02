package hexlet.code.app.dto.taskStatus;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TaskStatusCreateDTO {
    @Size(min = 1)
    @NotNull
    private String name;

    @Size(min = 1)
    @NotNull
    private String slug;
}
