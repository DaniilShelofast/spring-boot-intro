package mate.academy.project.dto.order;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class UpdateOrderRequestDto {
    @NotBlank
    private String status;
}
