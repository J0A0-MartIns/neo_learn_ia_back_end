package neo_learn_ia_api.Neo.Learn.Ia.API.repository;
import neo_learn_ia_api.Neo.Learn.Ia.API.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUserEmail(String username);
}
