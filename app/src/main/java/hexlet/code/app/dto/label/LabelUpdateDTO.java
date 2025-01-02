package hexlet.code.app.dto.label;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LabelUpdateDTO {
    @NotBlank
    private String name;
}
