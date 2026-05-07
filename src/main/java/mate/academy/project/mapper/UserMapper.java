package mate.academy.project.mapper;

import mate.academy.project.config.MapperConfig;
import mate.academy.project.dto.user.UserDto;
import mate.academy.project.dto.user.UserRegistrationDto;
import mate.academy.project.model.User;
import org.mapstruct.Mapper;

@Mapper(config = MapperConfig.class)
public interface UserMapper {
    UserDto toDto(User user);
    
    User toModel(UserRegistrationDto registrationDto);
}
