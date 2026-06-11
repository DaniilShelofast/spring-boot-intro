package mate.academy.project.service;

import java.util.List;
import mate.academy.project.dto.order.OrderRequestDto;
import mate.academy.project.dto.order.OrderResponseDto;
import mate.academy.project.dto.order.UpdateOrderRequestDto;

public interface OrderService {
    OrderResponseDto completeOrder(OrderRequestDto requestDto);

    List<OrderResponseDto> getAllOrders();

    OrderResponseDto statusUpdate(Long id, UpdateOrderRequestDto requestDto);
}
