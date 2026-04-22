package mate.academy.project.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import mate.academy.project.dto.user.UserRegistrationDto;

public class FieldMatchValidator implements ConstraintValidator<FieldMatch, UserRegistrationDto> {
    @Override
    public boolean isValid(UserRegistrationDto password, ConstraintValidatorContext context) {
        return password.getPassword().equals(password.getRepeatPassword());
    }
}
