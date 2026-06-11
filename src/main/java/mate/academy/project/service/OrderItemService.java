package mate.academy.project.service;

import java.util.List;
import mate.academy.project.dto.item.OrderItemResponseDto;

public interface OrderItemService {
    List<OrderItemResponseDto> getOrderItems(Long orderId);

    OrderItemResponseDto getItem(Long orderId, Long itemId);
}
