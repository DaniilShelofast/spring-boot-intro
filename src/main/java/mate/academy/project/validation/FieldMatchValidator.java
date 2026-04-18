package mate.academy.project.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import mate.academy.project.dto.user.PasswordDto;

public class FieldMatchValidator implements ConstraintValidator<FieldMatch, PasswordDto> {
    @Override
    public boolean isValid(PasswordDto password, ConstraintValidatorContext context) {
        return password.getPassword().equals(password.getRepeatPassword());
    }
}
