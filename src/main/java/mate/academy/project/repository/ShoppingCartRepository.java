package mate.academy.project.repository;

import java.util.Optional;
import mate.academy.project.model.ShoppingCart;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ShoppingCartRepository extends JpaRepository<ShoppingCart, Long> {
    @EntityGraph(attributePaths = {"cartItems", "cartItems.book"})
    Optional<ShoppingCart> findById(Long id);

    Optional<ShoppingCart> findByUserId(Long userId);
}
