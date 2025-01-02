package hexlet.code.app.dto.taskStatus;

import lombok.Getter;
import lombok.Setter;
import java.util.Date;

@Getter
@Setter
public class TaskStatusDTO {
    private Long id;

    private String name;

    private String slug;

    private Date createdAt;
}
