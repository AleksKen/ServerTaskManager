package app.code.dto.notification;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;
import java.util.Set;

@Getter
@Setter
public class NotificationDTO {
    private Long id;
    private Set<Long> teamIds;
    private String text;
    private Long taskId;
    private String type;
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Instant createdAt;
}
