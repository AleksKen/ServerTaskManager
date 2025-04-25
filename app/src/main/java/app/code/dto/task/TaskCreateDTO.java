package app.code.dto.task;

import lombok.Getter;
import lombok.Setter;

import java.time.Instant;
import java.util.Set;

@Getter
@Setter
public class TaskCreateDTO {
    private String title;
    private String description;
    private Instant deadline;
    private String priority;
    private String stage;
    private Set<Long> teamIds;
    private Set<String> assets;
    private Set<Long> taskLabelIds;
}
