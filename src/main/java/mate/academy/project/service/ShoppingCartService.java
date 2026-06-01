package mate.academy.project.service;

import mate.academy.project.dto.cart.CartItemRequestDto;
import mate.academy.project.dto.shopping.ShoppingCartResponseDto;
import mate.academy.project.dto.shopping.UpdateCartItemRequestDto;
import mate.academy.project.model.ShoppingCart;

public interface ShoppingCartService {
    ShoppingCartResponseDto getShoppingCart();

    ShoppingCartResponseDto addBookShoppingCart(CartItemRequestDto requestDto);

    ShoppingCartResponseDto updateCartQuantity(Long id, UpdateCartItemRequestDto requestDto);

    void deleteById(Long id);

    ShoppingCart createRegisterCart();
}
