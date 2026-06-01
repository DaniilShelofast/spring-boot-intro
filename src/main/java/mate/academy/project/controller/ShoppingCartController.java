package mate.academy.project.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import mate.academy.project.dto.cart.CartItemRequestDto;
import mate.academy.project.dto.shopping.ShoppingCartResponseDto;
import mate.academy.project.dto.shopping.UpdateCartItemRequestDto;
import mate.academy.project.service.ShoppingCartService;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Cart API", description = "Endpoints for managing cart")
@RestController
@RequiredArgsConstructor
@RequestMapping("/cart")
public class ShoppingCartController {
    private final ShoppingCartService shoppingCartService;

    @PreAuthorize("hasAuthority('ROLE_USER')")
    @GetMapping
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @Operation(summary = "get shopping cart", description = "get user shopping cart")
    public ShoppingCartResponseDto getShoppingCart() {
        return shoppingCartService.getShoppingCart();
    }

    @PreAuthorize("hasAuthority('ROLE_USER')")
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    @Operation(summary = "add book", description = "add a book to cart")
    public ShoppingCartResponseDto addBookShoppingCart(
            @RequestBody @Valid CartItemRequestDto requestDto) {
        return shoppingCartService.addBookShoppingCart(requestDto);
    }

    @PreAuthorize("hasAuthority('ROLE_USER')")
    @PutMapping("/cart-items/{id}")
    @Operation(summary = "update a quantity", description = "update a quantity by their unique id")
    public ShoppingCartResponseDto updateCartQuantity(@PathVariable Long id,
                                          @RequestBody @Valid UpdateCartItemRequestDto requestDto) {
        return shoppingCartService.updateCartQuantity(id, requestDto);
    }

    @PreAuthorize("hasAuthority('ROLE_USER')")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/cart-items/{id}")
    @Operation(summary = "delete item by id", description = "delete a item by their unique id")
    public void deleteById(@PathVariable Long id) {
        shoppingCartService.deleteById(id);
    }
}
