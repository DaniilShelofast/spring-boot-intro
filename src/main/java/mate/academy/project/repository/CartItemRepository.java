package mate.academy.project.repository;

import java.util.Optional;
import mate.academy.project.model.CartItem;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartItemRepository extends JpaRepository<CartItem, Long> {
    @EntityGraph(attributePaths = "shoppingCart")
    Optional<CartItem> findByIdAndShoppingCartId(Long id, Long shoppingCartId);
}
