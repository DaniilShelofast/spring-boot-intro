package mate.academy.project.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import mate.academy.project.dto.user.UserDto;
import mate.academy.project.dto.user.UserLoginRequestDto;
import mate.academy.project.dto.user.UserLoginResponseDto;
import mate.academy.project.dto.user.UserRegistrationDto;
import mate.academy.project.exception.RegistrationException;
import mate.academy.project.security.AuthenticationService;
import mate.academy.project.service.UserService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "User API", description = "Endpoints for managing users")
@RequiredArgsConstructor
@RestController
@RequestMapping("/auth")
public class AuthenticationController {
    private final UserService userService;
    private final AuthenticationService authenticationService;

    @PostMapping("/registration")
    @Operation(summary = "register", description = "user registration")
    public UserDto register(@RequestBody @Valid UserRegistrationDto request)
            throws RegistrationException {
        return userService.register(request);
    }

    @PostMapping("/login")
    @Operation(summary = "login", description = "user authorization")
    public UserLoginResponseDto login(@RequestBody @Valid UserLoginRequestDto request) {
        return authenticationService.authenticate(request);
    }
}
