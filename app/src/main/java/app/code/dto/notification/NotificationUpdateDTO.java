package app.code.dto.notification;

import lombok.Getter;
import lombok.Setter;
import org.openapitools.jackson.nullable.JsonNullable;

import java.util.Set;

@Getter
@Setter
public class NotificationUpdateDTO {
    private JsonNullable<Set<Long>> teamIds;
    private JsonNullable<String> text;
    private JsonNullable<Long> taskId;
    private JsonNullable<String> type;
}
