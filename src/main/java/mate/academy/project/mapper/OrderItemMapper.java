package mate.academy.project.mapper;

import mate.academy.project.config.MapperConfig;
import mate.academy.project.dto.item.OrderItemResponseDto;
import mate.academy.project.model.OrderItem;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(config = MapperConfig.class)
public interface OrderItemMapper {
    @Mapping(source = "book.id", target = "bookId")
    OrderItemResponseDto toDto(OrderItem orderItem);
}
