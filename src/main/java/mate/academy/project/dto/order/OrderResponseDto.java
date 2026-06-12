package mate.academy.project.dto.order;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import lombok.Data;
import mate.academy.project.dto.item.OrderItemResponseDto;

@Data
public class OrderResponseDto {
    private Long id;
    private Long userId;
    private List<OrderItemResponseDto> orderItems;
    private LocalDateTime orderDate;
    private BigDecimal total;
    private String status;
}
