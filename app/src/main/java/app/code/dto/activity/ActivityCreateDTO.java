package app.code.dto.activity;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ActivityCreateDTO {
    @NotBlank
    private String type;

    private String content;

    private Long taskId;
}
