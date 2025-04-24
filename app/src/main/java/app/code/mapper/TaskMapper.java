package app.code.mapper;

import app.code.dto.task.TaskCreateDTO;
import app.code.dto.task.TaskDTO;
import app.code.dto.task.TaskUpdateDTO;
import app.code.model.Label;
import app.code.model.Task;
import app.code.model.User;
import app.code.repository.LabelRepository;
import app.code.repository.UserRepository;
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
    private LabelRepository labelRepository;

    @Autowired
    private UserRepository userRepository;

    @Mapping(source = "name", target = "title")
    @Mapping(source = "labels", target = "taskLabelIds", qualifiedByName = "getLabelIds")
    public abstract TaskDTO map(Task taskStatus);


    @Mapping(target = "name", source = "title")
    @Mapping(target = "labels", source = "taskLabelIds", qualifiedByName = "getLabels")
    @Mapping(target = "team", source = "teamIds", qualifiedByName = "getTeam")
    public abstract Task map(TaskCreateDTO dto);

    @Mapping(target = "name", source = "title")
    @Mapping(target = "team", source = "teamIds", qualifiedByName = "getTeam")
    public abstract void update(TaskUpdateDTO dto, @MappingTarget Task model);


    @Named("getTeam")
    Set<User> getTeam(Set<Long> teamIds) {
        return userRepository.findAll()
                .stream()
                .filter(user -> Optional.ofNullable(teamIds)
                        .orElse(Collections.emptySet())
                        .contains(user.getId()))
                .collect(Collectors.toSet());
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
