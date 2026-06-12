package mate.academy.project.mapper;

import mate.academy.project.config.MapperConfig;
import mate.academy.project.dto.order.OrderRequestDto;
import mate.academy.project.dto.order.OrderResponseDto;
import mate.academy.project.dto.order.UpdateOrderRequestDto;
import mate.academy.project.model.Order;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(config = MapperConfig.class, uses = OrderItemMapper.class)
public interface OrderMapper {
    Order toEntity(OrderRequestDto requestDto);

    @Mapping(target = "userId", source = "user.id")
    OrderResponseDto toDto(Order order);

    @Mapping(target = "user", ignore = true)
    @Mapping(target = "total", ignore = true)
    @Mapping(target = "orderDate", ignore = true)
    @Mapping(target = "shippingAddress", ignore = true)
    @Mapping(target = "orderItems", ignore = true)
    void statusUpdate(@MappingTarget Order order, UpdateOrderRequestDto requestDto);
}
