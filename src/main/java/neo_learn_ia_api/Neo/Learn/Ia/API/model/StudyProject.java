package neo_learn_ia_api.Neo.Learn.Ia.API.model;


import jakarta.persistence.*;
import lombok.Data;
import neo_learn_ia_api.Neo.Learn.Ia.API.validation.annotations.DomainNotBlank;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@Table(name = "tb_study_projects")
public class StudyProject {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY )
    @Column(name = "id")
    private Long id;

    @DomainNotBlank(message = "O nome do projeto não pode ser nulo ou vazio")
    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "description")
    private String description;


    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    @JoinColumn(name = "study_project_id")
    private List<FileEntity> attachments = new ArrayList<>();

    @Column(name = "is_public", nullable = false)
    private boolean isPublic = false;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "owner_id", nullable = false)
    private User owner;

    @Column(name = "original_project_id", nullable = true)
    private Long originalProjectId;

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
    }
}
