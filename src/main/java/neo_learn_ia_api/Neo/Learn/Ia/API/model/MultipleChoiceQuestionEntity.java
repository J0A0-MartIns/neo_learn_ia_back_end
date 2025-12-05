package neo_learn_ia_api.Neo.Learn.Ia.API.model;


import jakarta.persistence.*;
import lombok.Data;
import neo_learn_ia_api.Neo.Learn.Ia.API.dto.QuestionContent;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.util.List;
@Entity
@Data
@Table(name = "multiple_choice_questions")
public class MultipleChoiceQuestionEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "data", columnDefinition = "jsonb")
    private QuestionContent data;

    public MultipleChoiceQuestionEntity() {}

    public MultipleChoiceQuestionEntity(QuestionContent data) {
        this.data = data;
    }

    public long getId() { return id; }
    public QuestionContent getData() { return data; }
    public void setData(QuestionContent data) { this.data = data; }
}

