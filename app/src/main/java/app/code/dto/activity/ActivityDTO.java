package app.code.dto.activity;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class ActivityDTO {
    private Long id;

    private String type;

    private String content;

    private LocalDate createdAt;
}
