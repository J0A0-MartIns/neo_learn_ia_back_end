package neo_learn_ia_api.Neo.Learn.Ia.API.model;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.util.Map;

@Entity
@Table(name = "study_schedule")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StudySchedule {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String userId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "study_project_id", nullable = false)
    private StudyProject project;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "schedule_data", columnDefinition = "jsonb")
    private Map<String, Object> scheduleData;
}
