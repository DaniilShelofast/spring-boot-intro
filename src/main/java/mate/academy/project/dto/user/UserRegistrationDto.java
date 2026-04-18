package mate.academy.project.dto.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import mate.academy.project.validation.FieldMatch;
import org.hibernate.validator.constraints.Length;

@Data
@FieldMatch
public class UserRegistrationDto implements PasswordDto {
    @NotBlank
    @Email
    private String email;
    @NotBlank
    @Length(min = 8, max = 20)
    private String password;
    @NotBlank
    @Length(min = 8, max = 20)
    private String repeatPassword;
    @NotBlank
    private String firstName;
    @NotBlank
    private String lastName;
    private String shippingAddress;
}
