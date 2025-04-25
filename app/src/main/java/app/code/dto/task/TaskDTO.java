package app.code.dto.task;

import app.code.dto.activity.ActivityDTO;
import app.code.dto.label.LabelDTO;
import app.code.dto.user.UserDTO;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;
import java.util.Set;

@Getter
@Setter
public class TaskDTO {
    private Long id;
    private String title;
    private String description;
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Instant deadline;
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Instant createdAt;
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Instant updatedAt;
    private String priority;
    private String stage;
    private Set<UserDTO> team;
    private Set<ActivityDTO> activities;
    private Set<String> assets;
    private Set<LabelDTO> labels;
}
