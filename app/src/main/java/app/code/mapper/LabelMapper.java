package app.code.mapper;

import app.code.dto.label.LabelCreateDTO;
import app.code.dto.label.LabelDTO;
import app.code.dto.label.LabelUpdateDTO;
import app.code.model.Label;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.MappingTarget;

@Mapper(
        uses = { JsonNullableMapper.class, ReferenceMapper.class },
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        componentModel = MappingConstants.ComponentModel.SPRING,
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE
)
public abstract class LabelMapper {
    public abstract Label map(LabelCreateDTO dto);
    public abstract LabelDTO map(Label label);
    public abstract LabelCreateDTO create(Label label);
    public abstract void  update(LabelUpdateDTO dto, @MappingTarget Label model);
}
