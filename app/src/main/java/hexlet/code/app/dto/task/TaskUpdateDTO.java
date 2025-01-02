package hexlet.code.app.dto.task;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.openapitools.jackson.nullable.JsonNullable;

@Getter
@Setter
public class TaskUpdateDTO {
    private JsonNullable<Long> index;
    private JsonNullable<Long> assigneeId;
    @NotBlank
    @Size(min = 1)
    private JsonNullable<String> title;
    private JsonNullable<String> content;
    @NotBlank
    @Size(min = 1)
    private JsonNullable<String> status;
}
