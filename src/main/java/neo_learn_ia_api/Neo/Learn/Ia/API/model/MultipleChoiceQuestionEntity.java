package neo_learn_ia_api.Neo.Learn.Ia.API.model;


import jakarta.persistence.*;
import lombok.Data;

import java.util.List;
import java.util.UUID;
@Entity
@Table(name = "multiple_choice_questions")
@Data
public class MultipleChoiceQuestionEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @Column(nullable = false, length = 1000)
    private String question;

    @ElementCollection
    @CollectionTable(name = "question_options", joinColumns = @JoinColumn(name = "question_id"))
    @Column(name = "option_text", length = 500)
    private List<String> options;

    @Column(nullable = false, length = 2)
    private String answer;

}

