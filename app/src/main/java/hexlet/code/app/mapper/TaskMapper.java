package hexlet.code.app.mapper;

import hexlet.code.app.dto.TaskCreateDTO;
import hexlet.code.app.dto.TaskDTO;
import hexlet.code.app.dto.TaskUpdateDTO;
import hexlet.code.app.model.Task;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.MappingTarget;

@Mapper(
        uses = { JsonNullableMapper.class },
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        componentModel = MappingConstants.ComponentModel.SPRING,
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE
)
public abstract class TaskMapper {
    @Mapping(source = "taskStatus.slug", target = "status")
    @Mapping(source = "assignee.id", target = "assigneeId")
    public abstract TaskDTO map(Task taskStatus);
    @Mapping(target = "taskStatus.slug", source = "status")
    @Mapping(target = "assignee.id", source = "assigneeId")
    public abstract Task map(TaskCreateDTO dto);
    @Mapping(target = "taskStatus.slug", source = "status")
    @Mapping(target = "assignee.id", source = "assigneeId")
    public abstract void update(TaskUpdateDTO dto, @MappingTarget Task model);
}
