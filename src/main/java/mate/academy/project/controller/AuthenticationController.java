package mate.academy.project.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import mate.academy.project.dto.user.UserDto;
import mate.academy.project.dto.user.UserRegistrationDto;
import mate.academy.project.exception.RegistrationException;
import mate.academy.project.service.UserService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/auth")
public class AuthenticationController {
    private final UserService userService;

    @PostMapping("/register")
    public UserDto register(@RequestBody @Valid UserRegistrationDto request)
            throws RegistrationException {
        return userService.register(request);
    }
}
