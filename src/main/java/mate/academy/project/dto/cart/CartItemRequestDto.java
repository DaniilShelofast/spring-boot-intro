package mate.academy.project.dto.cart;

import jakarta.persistence.Column;
import lombok.Data;

@Data
public class CartItemRequestDto {
    @Column(nullable = false)
    private Long bookId;
    @Column(nullable = false)
    private int quantity;
}
