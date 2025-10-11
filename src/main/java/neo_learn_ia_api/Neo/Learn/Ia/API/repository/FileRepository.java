package neo_learn_ia_api.Neo.Learn.Ia.API.repository;


import neo_learn_ia_api.Neo.Learn.Ia.API.model.FileEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface FileRepository extends JpaRepository<FileEntity, UUID> {

}
