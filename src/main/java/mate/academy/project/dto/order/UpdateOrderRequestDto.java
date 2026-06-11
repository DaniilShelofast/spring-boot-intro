package mate.academy.project.dto.order;

import jakarta.persistence.Column;
import lombok.Data;

@Data
public class UpdateOrderRequestDto {
    @Column(nullable = false)
    private String status;
}
