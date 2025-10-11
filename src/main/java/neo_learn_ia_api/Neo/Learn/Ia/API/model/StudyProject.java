package neo_learn_ia_api.Neo.Learn.Ia.API.model;


import jakarta.persistence.*;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Data
@Table(name = "tb_study_projects")
public class StudyProject {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "description")
    private String description;


    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    @JoinColumn(name = "study_project_id")
    private List<FileEntity> attachments = new ArrayList<>();

}
