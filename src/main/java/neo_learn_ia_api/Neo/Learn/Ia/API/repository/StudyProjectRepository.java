package neo_learn_ia_api.Neo.Learn.Ia.API.repository;

import neo_learn_ia_api.Neo.Learn.Ia.API.model.StudyProject;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface StudyProjectRepository  extends JpaRepository<StudyProject, Long> {
    boolean existsByNameIgnoreCase(String name);
    List<StudyProject> findByOwnerId(Long ownerId);
    List<StudyProject> findByOwnerIdNotAndIsPublicTrue(Long ownerId);
}
