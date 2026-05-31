package mate.academy.project.service.impl;

import java.util.Optional;
import lombok.RequiredArgsConstructor;
import mate.academy.project.dto.cart.CartItemRequestDto;
import mate.academy.project.dto.shopping.ShoppingCartResponseDto;
import mate.academy.project.dto.shopping.UpdateCartItemRequestDto;
import mate.academy.project.exception.EntityNotFoundException;
import mate.academy.project.mapper.CartItemMapper;
import mate.academy.project.mapper.ShoppingCartMapper;
import mate.academy.project.model.Book;
import mate.academy.project.model.CartItem;
import mate.academy.project.model.ShoppingCart;
import mate.academy.project.model.User;
import mate.academy.project.repository.BookRepository;
import mate.academy.project.repository.CartItemRepository;
import mate.academy.project.repository.ShoppingCartRepository;
import mate.academy.project.service.ShoppingCartService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class ShoppingCartServiceImpl implements ShoppingCartService {
    private final ShoppingCartRepository shoppingCartRepository;
    private final ShoppingCartMapper cartMapper;
    private final CartItemMapper cartItemMapper;
    private final BookRepository bookRepository;
    private final CartItemRepository cartItemRepository;

    @Override
    @Transactional(readOnly = true)
    public ShoppingCartResponseDto getShoppingCart() {
        Long userId = getUserId();
        ShoppingCart cart = getShoppingCartByUserId(userId);
        return cartMapper.toDto(cart);
    }

    @Override
    @Transactional
    public ShoppingCartResponseDto addBookShoppingCart(CartItemRequestDto requestDto) {
        Long userId = getUserId();
        ShoppingCart cart = getShoppingCartByUserId(userId);
        Book book = bookRepository.findById(requestDto.getBookId())
                .orElseThrow(() -> new EntityNotFoundException(
                        "book not found " + requestDto.getBookId()
                ));

        Optional<CartItem> item = cart.getCartItems().stream()
                .filter(cartItem -> cartItem.getBook().getId().equals(book.getId()))
                .findFirst();

        if (item.isPresent()) {
            CartItem cartItem = item.get();
            cartItem.setQuantity(cartItem.getQuantity()
                    + requestDto.getQuantity());
        } else {
            CartItem newItem = cartItemMapper.toEntity(requestDto);
            newItem.setBook(book);
            newItem.setShoppingCart(cart);
            newItem.setQuantity(requestDto.getQuantity());
            cart.getCartItems().add(newItem);
        }

        shoppingCartRepository.save(cart);
        return cartMapper.toDto(cart);
    }

    @Override
    @Transactional
    public ShoppingCartResponseDto updateCartQuantity(Long id,
                                                      UpdateCartItemRequestDto requestDto) {
        Long userId = getUserId();
        ShoppingCart cart = getShoppingCartByUserId(userId);
        CartItem cartItem = getCartItemByIdAndCartId(id, cart.getId());
        cartItemMapper.updateQuantity(cartItem, requestDto);
        cartItemRepository.save(cartItem);
        return cartMapper.toDto(cart);
    }

    @Override
    public void deleteById(Long id) {
        Long userId = getUserId();
        ShoppingCart cart = getShoppingCartByUserId(userId);
        CartItem cartItem = getCartItemByIdAndCartId(id, cart.getId());
        cartItemRepository.deleteById(cartItem.getId());
    }

    @Override
    public ShoppingCart createRegisterCart() {
        return new ShoppingCart();
    }

    private CartItem getCartItemByIdAndCartId(Long id, Long cartId) {
        return cartItemRepository.findByIdAndShoppingCartId(id, cartId)
                .orElseThrow(() -> new EntityNotFoundException(
                        "cart not found " + cartId
                ));
    }

    private ShoppingCart getShoppingCartByUserId(Long userId) {
        return shoppingCartRepository
                .findByUserId(userId)
                .orElseThrow(() -> new EntityNotFoundException(
                        "user not found " + userId
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
