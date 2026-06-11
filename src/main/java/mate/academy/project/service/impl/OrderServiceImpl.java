package mate.academy.project.service.impl;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import mate.academy.project.dto.order.OrderRequestDto;
import mate.academy.project.dto.order.OrderResponseDto;
import mate.academy.project.dto.order.UpdateOrderRequestDto;
import mate.academy.project.exception.EntityNotFoundException;
import mate.academy.project.mapper.OrderMapper;
import mate.academy.project.model.CartItem;
import mate.academy.project.model.Order;
import mate.academy.project.model.OrderItem;
import mate.academy.project.model.ShoppingCart;
import mate.academy.project.model.Status;
import mate.academy.project.model.User;
import mate.academy.project.repository.OrderRepository;
import mate.academy.project.repository.ShoppingCartRepository;
import mate.academy.project.service.OrderService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {
    private final DateTimeFormatter inputFormatter = DateTimeFormatter
            .ofPattern("yyyy-MM-dd'T'HH:mm:ss");
    private final ShoppingCartRepository shoppingCartRepository;
    private final OrderRepository orderRepository;
    private final OrderMapper orderMapper;

    @Override
    @Transactional
    public OrderResponseDto completeOrder(OrderRequestDto requestDto) {
        User user = getUser();
        Order order = orderMapper.toEntity(requestDto);
        order.setUser(user);
        order.setStatus(Status.COMPLETED);
        order.setOrderDate(LocalDateTime.parse(LocalDateTime.now().format(inputFormatter)));
        order.setShippingAddress(requestDto.getShippingAddress());
        user.setShippingAddress(requestDto.getShippingAddress());
        Set<OrderItem> orderItems = new HashSet<>();
        BigDecimal countPrice = BigDecimal.valueOf(0.0);
        ShoppingCart cart = getShoppingCartByUserId(user.getId());
        for (CartItem cartItem : cart.getCartItems()) {
            OrderItem orderItem = new OrderItem();
            orderItem.setOrder(order);
            orderItem.setBook(cartItem.getBook());
            orderItem.setQuantity(cartItem.getQuantity());
            orderItem.setPrice(cartItem.getBook().getPrice());
            BigDecimal itemTotalPrice = orderItem.getPrice()
                    .multiply(BigDecimal.valueOf(orderItem.getQuantity()));
            countPrice = countPrice.add(itemTotalPrice);
            orderItems.add(orderItem);
        }
        order.setTotal(countPrice);
        order.setOrderItems(orderItems);
        cart.getCartItems().clear();
        orderRepository.save(order);
        return orderMapper.toDto(order);
    }

    @Override
    @Transactional(readOnly = true)
    public List<OrderResponseDto> getAllOrders() {
        return orderRepository.findAllByUserId(getUser().getId())
                .stream()
                .map(orderMapper::toDto)
                .toList();
    }

    @Override
    @Transactional
    public OrderResponseDto statusUpdate(Long id, UpdateOrderRequestDto requestDto) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Can't get order by id " + id));
        orderMapper.statusUpdate(order, requestDto);
        orderRepository.save(order);
        return orderMapper.toDto(order);
    }

    private ShoppingCart getShoppingCartByUserId(Long userId) {
        return shoppingCartRepository
                .findByUserId(userId)
                .orElseThrow(() -> new EntityNotFoundException(
                        "user not found " + userId
                ));
    }

    private User getUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !auth.isAuthenticated()) {
            throw new RuntimeException("User is not authenticated");
        }
        return (User) auth.getPrincipal();
    }
}
