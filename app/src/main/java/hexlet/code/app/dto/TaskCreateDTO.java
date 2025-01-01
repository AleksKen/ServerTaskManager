package hexlet.code.app.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TaskCreateDTO {
    private Long index;
    private Long assigneeId;
    @NotBlank
    @Size(min = 1)
    private String title;
    private String content;
    @NotBlank
    @Size(min = 1)
    private String status;
}
