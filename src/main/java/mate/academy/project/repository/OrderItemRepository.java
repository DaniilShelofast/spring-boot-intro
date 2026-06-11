package mate.academy.project.repository;

import java.util.List;
import java.util.Optional;
import mate.academy.project.model.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {
    List<OrderItem> findAllByOrderId(Long orderId);

    Optional<OrderItem> findByIdAndOrderId(Long id, Long orderId);
}
