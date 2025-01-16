package hexlet.code.mapper;

import hexlet.code.dto.task.TaskCreateDTO;
import hexlet.code.dto.task.TaskDTO;
import hexlet.code.dto.task.TaskUpdateDTO;
import hexlet.code.exception.ResourceNotFoundException;
import hexlet.code.model.Label;
import hexlet.code.model.Task;
import hexlet.code.model.TaskStatus;
import hexlet.code.model.User;
import hexlet.code.repository.LabelRepository;
import hexlet.code.repository.TaskStatusRepository;
import hexlet.code.repository.UserRepository;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Collections;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Mapper(
        uses = { JsonNullableMapper.class, ReferenceMapper.class },
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        componentModel = MappingConstants.ComponentModel.SPRING,
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE
)
public abstract class TaskMapper {
    @Autowired
    private TaskStatusRepository taskStatusRepository;

    @Autowired
    private LabelRepository labelRepository;

    @Autowired
    private UserRepository userRepository;

    @Mapping(source = "taskStatus.slug", target = "status")
    @Mapping(source = "assignee.id", target = "assigneeId")
    @Mapping(source = "name", target = "title")
    @Mapping(source = "description", target = "content")
    @Mapping(source = "labels", target = "taskLabelIds", qualifiedByName = "getLabelIds")
    public abstract TaskDTO map(Task taskStatus);

    @Mapping(target = "taskStatus", source = "status")
    @Mapping(target = "assignee", source = "assigneeId", qualifiedByName = "getAssignee")
    @Mapping(target = "name", source = "title")
    @Mapping(target = "description", source = "content")
    @Mapping(target = "labels", source = "taskLabelIds", qualifiedByName = "getLabels")
    public abstract Task map(TaskCreateDTO dto);

    @Mapping(target = "taskStatus", source = "status")
    @Mapping(target = "assignee", source = "assigneeId", qualifiedByName = "getAssignee")
    @Mapping(target = "name", source = "title")
    @Mapping(target = "description", source = "content")
    @Mapping(target = "labels", source = "taskLabelIds", qualifiedByName = "getLabels")
    public abstract void update(TaskUpdateDTO dto, @MappingTarget Task model);

    public TaskStatus toEntity(String slug) {
        return taskStatusRepository.findBySlug(slug)
                .orElseThrow(() -> new ResourceNotFoundException("Status not found"));
    }

    @Named("getAssignee")
    User getAssignee(Long assigneeId) {
        if (assigneeId == null) {
            return null;
        }
        return userRepository.findById(assigneeId).get();
    }

    @Named("getLabelIds")
    Set<Long> getLabelIds(Set<Label> labels) {
        return labels.stream()
                .map(Label::getId)
                .collect(Collectors.toSet());
    }

    @Named("getLabels")
    Set<Label> getLabels(Set<Long> labelIds) {
        return labelRepository.findAll()
                .stream()
                .filter(label -> Optional.ofNullable(labelIds)
                        .orElse(Collections.emptySet())
                        .contains(label.getId()))
                .collect(Collectors.toSet());
    }
}
