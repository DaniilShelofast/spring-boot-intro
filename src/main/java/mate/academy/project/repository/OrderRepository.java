package mate.academy.project.repository;

import java.util.List;
import java.util.Optional;
import mate.academy.project.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findAllByUserId(Long userId);

    Optional<Order> findByIdAndUserId(Long id, Long userId);
}
