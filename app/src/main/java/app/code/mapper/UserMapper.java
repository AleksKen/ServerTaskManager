package app.code.mapper;

import app.code.dto.user.UserCreateDTO;
import app.code.dto.user.UserDTO;
import app.code.dto.user.UserUpdateDTO;
import app.code.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.MappingTarget;
import org.mapstruct.BeforeMapping;
import org.openapitools.jackson.nullable.JsonNullable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;

@Mapper(
        uses = { JsonNullableMapper.class, ReferenceMapper.class },
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
        componentModel = MappingConstants.ComponentModel.SPRING,
        unmappedTargetPolicy = ReportingPolicy.IGNORE
)
public abstract class UserMapper {
    @Autowired
    private PasswordEncoder passwordEncoder;

    public abstract UserDTO map(User user);
    public abstract User map(UserCreateDTO dto);
    public abstract UserCreateDTO build(User user);
    public abstract void update(UserUpdateDTO dto, @MappingTarget User model);

    @BeforeMapping
    public void encryptPassword(UserCreateDTO data) {
        if (data.getPassword() != null) {
            String password = data.getPassword();
            data.setPassword(passwordEncoder.encode(password));
        }
    }

    @BeforeMapping
    public void encryptPassword(UserUpdateDTO data) {
        if (data.getPassword() != null) {
            String password = data.getPassword().get();
            data.setPassword(JsonNullable.of(passwordEncoder.encode(password)));
        }
    }
}
