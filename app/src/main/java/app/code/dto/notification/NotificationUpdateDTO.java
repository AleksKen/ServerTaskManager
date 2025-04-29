package app.code.dto.notification;

import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
public class NotificationUpdateDTO {
    private Set<Long> teamIds;
    private String text;
    private Long taskId;
    private String type;
}
