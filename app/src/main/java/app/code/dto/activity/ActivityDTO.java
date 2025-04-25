package app.code.dto.activity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

@Getter
@Setter
public class ActivityDTO {
    private Long id;

    private String type;

    private String content;

    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Instant createdAt;
}
