package mate.academy.project.repository;

import java.util.Optional;
import mate.academy.project.dto.user.UserDto;
import mate.academy.project.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<UserDto> findByEmail(String email);
}
