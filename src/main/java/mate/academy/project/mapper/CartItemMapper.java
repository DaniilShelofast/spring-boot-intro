package mate.academy.project.mapper;

import mate.academy.project.config.MapperConfig;
import mate.academy.project.dto.cart.CartItemDto;
import mate.academy.project.dto.cart.CartItemRequestDto;
import mate.academy.project.dto.shopping.UpdateCartItemRequestDto;
import mate.academy.project.model.CartItem;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(config = MapperConfig.class, uses = BookMapper.class)
public interface CartItemMapper {
    @Mapping(source = "bookId", target = "book", qualifiedByName = "bookFromId")
    CartItem toEntity(CartItemRequestDto dto);

    @Mapping(source = "book.id", target = "bookId")
    @Mapping(source = "book.title", target = "bookTitle")
    CartItemDto toDto(CartItem cartItem);

    @Mapping(target = "shoppingCart", ignore = true)
    @Mapping(target = "book", ignore = true)
    void updateQuantity(@MappingTarget CartItem cartItem,
                        UpdateCartItemRequestDto updateCartItemRequestDto);
}
