package mate.academy.project.service.impl;

import java.util.Set;
import lombok.RequiredArgsConstructor;
import mate.academy.project.dto.user.UserDto;
import mate.academy.project.dto.user.UserRegistrationDto;
import mate.academy.project.exception.EntityNotFoundException;
import mate.academy.project.exception.RegistrationException;
import mate.academy.project.mapper.UserMapper;
import mate.academy.project.model.Role;
import mate.academy.project.model.RoleName;
import mate.academy.project.model.ShoppingCart;
import mate.academy.project.model.User;
import mate.academy.project.repository.RoleRepository;
import mate.academy.project.repository.UserRepository;
import mate.academy.project.service.ShoppingCartService;
import mate.academy.project.service.UserService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;
    private final RoleRepository roleRepository;
    private final ShoppingCartService cartService;

    @Override
    @Transactional
    public UserDto register(UserRegistrationDto request) throws RegistrationException {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new RegistrationException("User with this email already exists " + request);
        }
        User user = userMapper.toModel(request);
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        Role role = roleRepository.findByRoleName(RoleName.ROLE_USER)
                .orElseThrow(() -> new EntityNotFoundException(RoleName.ROLE_USER + " not found"));
        user.setRoles(Set.of(role));
        ShoppingCart cart = cartService.createRegisterCart();
        user.setShoppingCart(cart);
        cart.setUser(user);
        userRepository.save(user);
        return userMapper.toDto(user);
    }
}
