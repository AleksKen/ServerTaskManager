package app.code.dto.task;

import app.code.dto.activity.ActivityDTO;
import app.code.dto.user.UserDTO;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

import java.util.Set;

@Getter
@Setter
public class TaskDTO {
    private Long id;
    private String title;
    private String description;
    private LocalDate deadline;
    private LocalDate createdAt;
    private LocalDate updatedAt;
    private String priority;
    private String stage;
    private Set<UserDTO> team;
    private Set<ActivityDTO> activities;
    private Set<String> assets;
    private Set<Long> taskLabelIds;
}
