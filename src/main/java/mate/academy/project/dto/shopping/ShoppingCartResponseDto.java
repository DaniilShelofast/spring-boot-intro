package mate.academy.project.dto.shopping;

import java.util.List;
import lombok.Data;
import mate.academy.project.dto.cart.CartItemDto;

@Data
public class ShoppingCartResponseDto {
    private Long id;
    private Long userId;
    private List<CartItemDto> cartItems;
}
