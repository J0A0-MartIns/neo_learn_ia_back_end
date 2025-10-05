package neo_learn_ia_api.Neo.Learn.Ia.API.repository;

import neo_learn_ia_api.Neo.Learn.Ia.API.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
    Role findByName(String name);
}
