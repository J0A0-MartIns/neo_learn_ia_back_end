package neo_learn_ia_api.Neo.Learn.Ia.API.repository;


import neo_learn_ia_api.Neo.Learn.Ia.API.model.StudyProject;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface StudyProjectRepository  extends JpaRepository<StudyProject, UUID> {
    boolean existsByNameIgnoreCase(String name);
}
