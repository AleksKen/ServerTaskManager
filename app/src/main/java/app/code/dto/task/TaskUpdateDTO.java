package app.code.dto.task;

import lombok.Getter;
import lombok.Setter;
import org.openapitools.jackson.nullable.JsonNullable;

import java.time.LocalDate;
import java.util.Set;

@Getter
@Setter
public class TaskUpdateDTO {
    private JsonNullable<String> title;
    private JsonNullable<String> description;
    private JsonNullable<LocalDate> deadline;
    private JsonNullable<String> priority;
    private JsonNullable<String> stage;
    private JsonNullable<Set<Long>> teamIds;
    private JsonNullable<Set<String>> assets;
    private JsonNullable<Set<Long>> taskLabelIds;
}
