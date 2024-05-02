package clear.solution.userapp.repository;

import clear.solution.userapp.model.User;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findById(Long userId);

    Optional<User> findByEmail(String email);

    List<User> getAllByBirthDateAfterAndBirthDateBefore(LocalDate from, LocalDate to);
}
