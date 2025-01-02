package hexlet.code.app.dto.task;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class TaskDTO {
    private Long id;
    private Long index;
    private Date createdAt;
    private String status;
    private Long assigneeId;
    private String title;
    private String content;
}
