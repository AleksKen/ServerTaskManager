package app.code.mapper;

import app.code.dto.taskStatus.TaskStatusCreateDTO;
import app.code.dto.taskStatus.TaskStatusDTO;
import app.code.dto.taskStatus.TaskStatusUpdateDTO;
import app.code.model.TaskStatus;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.MappingTarget;

@Mapper(
        uses = { JsonNullableMapper.class },
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
        componentModel = MappingConstants.ComponentModel.SPRING,
        unmappedTargetPolicy = ReportingPolicy.IGNORE
)
public abstract class TaskStatusMapper {
    public abstract TaskStatusDTO map(TaskStatus taskStatus);
    public abstract TaskStatus map(TaskStatusCreateDTO dto);
    public abstract void update(TaskStatusUpdateDTO dto, @MappingTarget TaskStatus model);
}
