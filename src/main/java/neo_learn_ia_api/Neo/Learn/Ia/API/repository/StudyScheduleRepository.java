package neo_learn_ia_api.Neo.Learn.Ia.API.repository;

import neo_learn_ia_api.Neo.Learn.Ia.API.model.StudySchedule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface StudyScheduleRepository extends JpaRepository<StudySchedule, Long> {

    @Query("SELECT s FROM StudySchedule s JOIN FETCH s.project")
    List<StudySchedule> findAllWithProject();
}
