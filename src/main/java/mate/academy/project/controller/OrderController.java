package mate.academy.project.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import mate.academy.project.dto.item.OrderItemResponseDto;
import mate.academy.project.dto.order.OrderRequestDto;
import mate.academy.project.dto.order.OrderResponseDto;
import mate.academy.project.dto.order.UpdateOrderRequestDto;
import mate.academy.project.service.OrderService;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Orders API", description = "Endpoints for managing orders")
@RequiredArgsConstructor
@RestController
@RequestMapping("/orders")
public class OrderController {
    private final OrderService orderService;

    @PreAuthorize("hasAuthority('ROLE_USER')")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Create a new shipping address", description = "Create a new category")
    public OrderResponseDto addShippingAddress(@RequestBody @Valid OrderRequestDto requestDto) {
        return orderService.completeOrder(requestDto);
    }

    @PreAuthorize("hasAuthority('ROLE_USER')")
    @GetMapping
    @Operation(summary = "Get all orders", description = "Get a list of all orders")
    public List<OrderResponseDto> getAllOrders() {
        return orderService.getAllOrders();
    }

    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @PutMapping("/{id}")
    @Operation(summary = "Update status by id", description = "Update a status by their unique ID")
    public OrderResponseDto statusUpdate(@PathVariable Long id,
                                         @RequestBody @Valid UpdateOrderRequestDto requestDto) {
        return orderService.statusUpdate(id, requestDto);
    }

    @PreAuthorize("hasAuthority('ROLE_USER')")
    @GetMapping("/{orderId}/order-items/{itemId}")
    @Operation(summary = "Get item by id", description = "Get item and order by their unique ID")
    public OrderItemResponseDto getItem(@PathVariable Long itemId, @PathVariable Long orderId) {
        return orderService.getItem(itemId, orderId);
    }

    @PreAuthorize("hasAuthority('ROLE_USER')")
    @GetMapping("/{orderId}/order-items")
    @Operation(summary = "Get all order items", description = "get all order items")
    public List<OrderItemResponseDto> getOrderItems(@PathVariable Long orderId) {
        return orderService.getOrderItems(orderId);
    }
}
