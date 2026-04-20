package mate.academy.project.service.impl;

import lombok.RequiredArgsConstructor;
import mate.academy.project.dto.user.UserDto;
import mate.academy.project.dto.user.UserRegistrationDto;
import mate.academy.project.exception.RegistrationException;
import mate.academy.project.mapper.UserMapper;
import mate.academy.project.model.User;
import mate.academy.project.repository.UserRepository;
import mate.academy.project.service.UserService;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @Override
    public UserDto register(UserRegistrationDto request) throws RegistrationException {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new RegistrationException("User with this email already exists");
        }
        User user = userMapper.toModel(request);
        userRepository.save(user);
        return userMapper.toDto(user);
    }
}
