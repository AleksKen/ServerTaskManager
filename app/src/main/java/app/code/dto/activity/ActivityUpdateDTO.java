package app.code.dto.activity;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import org.openapitools.jackson.nullable.JsonNullable;

@Getter
@Setter
public class ActivityUpdateDTO {
    @NotBlank
    private JsonNullable<String> type;

    private JsonNullable<String> content;
}
