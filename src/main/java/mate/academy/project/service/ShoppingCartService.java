package mate.academy.project.service;

import mate.academy.project.dto.cart.CartItemDto;
import mate.academy.project.dto.cart.CartItemRequestDto;
import mate.academy.project.dto.shopping.ShoppingCartResponseDto;
import mate.academy.project.dto.shopping.UpdateCartItemRequestDto;

public interface ShoppingCartService {
    ShoppingCartResponseDto getShoppingCart();

    ShoppingCartResponseDto addBookShoppingCart(CartItemRequestDto requestDto);

    CartItemDto updateCartQuantity(Long id, UpdateCartItemRequestDto requestDto);

    void deleteById(Long id);
}
