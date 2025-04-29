package app.code.mapper;

import app.code.dto.notification.NotificationCreateDTO;
import app.code.dto.notification.NotificationDTO;
import app.code.dto.notification.NotificationUpdateDTO;
import app.code.model.Notification;
import app.code.model.User;
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
public abstract class NotificationMapper {
    @Autowired
    private UserRepository userRepository;

    @Mapping(target = "teamIds", source = "team", qualifiedByName = "getTeamIds")
    public abstract NotificationDTO map(Notification notification);

    @Mapping(target = "team", source = "teamIds", qualifiedByName = "getTeam")
    public abstract Notification map(NotificationCreateDTO dto);

    @Mapping(target = "team", source = "teamIds", qualifiedByName = "getTeam")
    public abstract void update(NotificationUpdateDTO dto, @MappingTarget Notification model);

    @Named("getTeam")
    Set<User> getTeam(Set<Long> teamIds) {
        return userRepository.findAll()
                .stream()
                .filter(user -> Optional.ofNullable(teamIds)
                        .orElse(Collections.emptySet())
                        .contains(user.getId()))
                .collect(Collectors.toSet());
    }

    @Named("getTeamIds")
    Set<Long> getTeamIds(Set<User> team) {
        return team.stream()
                .map(User::getId)
                .collect(Collectors.toSet());
    }
}
