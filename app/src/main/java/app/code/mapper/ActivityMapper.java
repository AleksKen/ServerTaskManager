package app.code.mapper;

import app.code.dto.activity.ActivityCreateDTO;
import app.code.dto.activity.ActivityDTO;
import app.code.dto.activity.ActivityUpdateDTO;
import app.code.model.Activity;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.MappingConstants;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(
        uses = { JsonNullableMapper.class, ReferenceMapper.class },
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        componentModel = MappingConstants.ComponentModel.SPRING,
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE
)

public abstract class ActivityMapper {
    @Mapping(target = "task", source = "taskId")
    public abstract Activity map(ActivityCreateDTO dto);
    public abstract ActivityDTO map(Activity activity);
    @Mapping(target = "taskId", source = "task.id")
    public abstract ActivityCreateDTO create(Activity activity);
    public abstract void  update(ActivityUpdateDTO dto, @MappingTarget Activity model);
}
