package mate.academy.project.service.impl;

import java.util.List;
import lombok.RequiredArgsConstructor;
import mate.academy.project.dto.item.OrderItemResponseDto;
import mate.academy.project.exception.EntityNotFoundException;
import mate.academy.project.mapper.OrderItemMapper;
import mate.academy.project.model.Order;
import mate.academy.project.model.OrderItem;
import mate.academy.project.model.User;
import mate.academy.project.repository.OrderItemRepository;
import mate.academy.project.repository.OrderRepository;
import mate.academy.project.service.OrderItemService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OrderItemServiceImpl implements OrderItemService {
    private final OrderItemMapper orderItemMapper;
    private final OrderItemRepository orderItemRepository;
    private final OrderRepository orderRepository;

    @Override
    public List<OrderItemResponseDto> getOrderItems(Long orderId) {
        Long id = getOrderId(orderId);
        return orderItemRepository.findAllByOrderId(id)
                .stream()
                .map(orderItemMapper::toDto)
                .toList();
    }

    @Override
    public OrderItemResponseDto getItem(Long itemId, Long orderId) {
        Long id = getOrderId(orderId);
        OrderItem item = orderItemRepository.findByIdAndOrderId(itemId, id)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Can`t get item and order by id" + itemId
                ));
        return orderItemMapper.toDto(item);
    }

    private Long getOrderId(Long orderId) {
        return orderRepository.findByIdAndUserId(orderId, getUserId())
                .map(Order::getId)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Order not found with id: " + orderId
                ));
    }

    private Long getUserId() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !auth.isAuthenticated()) {
            throw new RuntimeException("User is not authenticated");
        }
        User user = (User) auth.getPrincipal();
        return user.getId();
    }
}
