package mate.academy.project.service;

import mate.academy.project.dto.user.UserDto;
import mate.academy.project.dto.user.UserRegistrationDto;
import mate.academy.project.exception.RegistrationException;

public interface UserService {
    UserDto register(UserRegistrationDto request) throws RegistrationException;
}
