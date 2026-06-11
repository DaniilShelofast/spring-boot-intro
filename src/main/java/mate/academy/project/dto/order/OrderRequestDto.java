package mate.academy.project.dto.order;

import jakarta.persistence.Column;
import lombok.Data;

@Data
public class OrderRequestDto {
    @Column(nullable = false)
    private String shippingAddress;
}
