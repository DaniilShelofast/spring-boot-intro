package mate.academy.project.dto.category;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class CreateCategoryDto {
    @NotBlank
    private String name;
    private String description;
}
