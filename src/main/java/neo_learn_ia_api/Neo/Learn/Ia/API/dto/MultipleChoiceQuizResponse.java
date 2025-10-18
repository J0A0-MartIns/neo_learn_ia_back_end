package neo_learn_ia_api.Neo.Learn.Ia.API.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;
@Getter
@Setter
public class MultipleChoiceQuizResponse {
    private String question;
    private List<String> options;
    private String answer;

}