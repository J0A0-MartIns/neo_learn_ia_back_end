package neo_learn_ia_api.Neo.Learn.Ia.API.repository;

import neo_learn_ia_api.Neo.Learn.Ia.API.model.FileEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FileRepository extends JpaRepository<FileEntity, Long> {
    List<FileEntity> findByStudyProjectId(Long projectId);
}
