package mate.academy.project.service.impl;

import lombok.RequiredArgsConstructor;
import mate.academy.project.dto.cart.CartItemDto;
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
import mate.academy.project.repository.UserRepository;
import mate.academy.project.service.ShoppingCartService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class ShoppingCartServiceImpl implements ShoppingCartService {
    private final ShoppingCartRepository shoppingCartRepository;
    private final ShoppingCartMapper cartMapper;
    private final CartItemMapper cartItemMapper;
    private final UserRepository userRepository;
    private final BookRepository bookRepository;
    private final CartItemRepository cartItemRepository;

    @Override
    @Transactional(readOnly = true)
    public ShoppingCartResponseDto getShoppingCart() {
        User user = getAuthenticatedUser();
        ShoppingCart cart = shoppingCartRepository
                .findById(user.getShoppingCart().getId())
                .orElseThrow(() -> new EntityNotFoundException("can't get shopping cart by id"));
        return cartMapper.toDto(cart);
    }

    @Override
    @Transactional
    public ShoppingCartResponseDto addBookShoppingCart(CartItemRequestDto requestDto) {
        User user = getAuthenticatedUser();
        ShoppingCart cart = shoppingCartRepository
                .findById(user.getShoppingCart().getId())
                .orElseThrow(() -> new EntityNotFoundException("can't get shopping cart by id"));
        CartItem cartItem = cartItemMapper.toEntity(requestDto);
        cartItem.setShoppingCart(cart);
        Book book = bookRepository.findById(requestDto.getBookId())
                .orElseThrow(() -> new EntityNotFoundException("book not found"));
        cartItem.setBook(book);
        cartItem.setQuantity(requestDto.getQuantity());
        cart.getCartItems().add(cartItem);
        shoppingCartRepository.save(cart);
        return cartMapper.toDto(cart);
    }

    @Override
    public CartItemDto updateCartQuantity(Long id, UpdateCartItemRequestDto requestDto) {
        CartItem cartItem = cartItemRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("can't get cart item"));
        cartItemMapper.updateQuantity(cartItem, requestDto);
        cartItemRepository.save(cartItem);
        return cartItemMapper.toDto(cartItem);
    }

    @Override
    public void deleteById(Long id) {
        cartItemRepository.deleteById(id);
    }

    private User getAuthenticatedUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !auth.isAuthenticated()) {
            throw new RuntimeException("User is not authenticated");
        }
        UserDetails userDetails = (UserDetails) auth.getPrincipal();
        if (userDetails == null) {
            throw new UsernameNotFoundException("User details not found");
        }
        return userRepository.findByEmail(userDetails.getUsername())
                .orElseThrow(() -> new EntityNotFoundException("can't get user by name"));
    }
}
