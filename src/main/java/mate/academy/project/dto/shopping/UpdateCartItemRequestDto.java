package mate.academy.project.dto.shopping;

import jakarta.persistence.Column;
import lombok.Data;

@Data
public class UpdateCartItemRequestDto {
    @Column(nullable = false)
    private int quantity;
}
