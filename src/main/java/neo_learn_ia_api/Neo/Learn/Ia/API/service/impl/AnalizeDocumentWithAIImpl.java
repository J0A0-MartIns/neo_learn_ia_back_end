package neo_learn_ia_api.Neo.Learn.Ia.API.service.impl;


import neo_learn_ia_api.Neo.Learn.Ia.API.dto.MultipleChoiceQuizResponse;
import neo_learn_ia_api.Neo.Learn.Ia.API.dto.ScheduleRequest;
import neo_learn_ia_api.Neo.Learn.Ia.API.model.FileEntity;
import neo_learn_ia_api.Neo.Learn.Ia.API.repository.FileRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import neo_learn_ia_api.Neo.Learn.Ia.API.model.MultipleChoiceQuestionEntity;
import neo_learn_ia_api.Neo.Learn.Ia.API.repository.MultipleChoiceQuestionRepository;
import neo_learn_ia_api.Neo.Learn.Ia.API.service.AnalizeDocumentWithAI;
import neo_learn_ia_api.Neo.Learn.Ia.API.service.openAi.OpenAIService;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import reactor.core.publisher.Mono;

import java.io.File;
import java.io.IOException;
import java.util.List;

@Service
@AllArgsConstructor
public class AnalizeDocumentWithAIImpl implements AnalizeDocumentWithAI {

    private final OpenAIService openAIService;
    private final MultipleChoiceQuestionRepository repository;
    private final FileRepository fileRepository;
    private final ObjectMapper objectMapper = new ObjectMapper();
    private static final Logger logger = LoggerFactory.getLogger(AnalizeDocumentWithAIImpl.class);

    public Mono<List<MultipleChoiceQuestionEntity>> generateMultipleChoiceQuestions(MultipartFile file) throws IOException {
        String prompt = "Baseado no arquivo no file_id enviado, gere 5 questões de múltipla escolha com 4 alternativas cada e forneça a resposta correta. "
                + "Responda apenas em JSON no seguinte formato: "
                + "[{\"question\": \"...\", \"options\": [\"A\", \"B\", \"C\", \"D\"], \"answer\": \"A\"}]\n\n";


        return openAIService.getChatCompletionWithFile(file, prompt)
                .flatMap(jsonResponse -> {
                    try {
                        List<MultipleChoiceQuestionEntity> questions = objectMapper.readValue(
                                jsonResponse,
                                new com.fasterxml.jackson.core.type.TypeReference<List<MultipleChoiceQuestionEntity>>() {}
                        );

                        logger.debug("Desserialização concluída. {} questões detectadas.", questions.size());
                        for (int i = 0; i < questions.size(); i++) {
                            MultipleChoiceQuestionEntity q = questions.get(i);
                        }

                        List<MultipleChoiceQuestionEntity> saved = repository.saveAll(questions);
                        logger.debug("{} questões salvas no banco com sucesso.", saved.size());

                        return Mono.just(saved);
                    } catch (Exception e) {
                        logger.error("Erro ao processar e salvar questões", e);
                        return Mono.error(new RuntimeException("Erro ao processar e salvar questões: " + e.getMessage(), e));
                    }
                });
    }

    public Mono<List<MultipleChoiceQuizResponse>> getAllQuestions() {
        return Mono.fromSupplier(() ->
                repository.findAll().stream()
                        .map(MultipleChoiceQuizResponse::fromEntity)
                        .toList()
        );
    }

    public Mono<String> createScheduleWithFile(ScheduleRequest request) throws IOException{
        String prompt = String.format("""
                                      Baseado no arquivo no file_id enviado, avalie os tópicos de estudo e gera um cronograma levando as seguintes considerações:
                                      O cronamgra terá duração de 3 semanas.
                                      A distribuição de horas deve levar em conta a dificuldade de cada matéria.
                                      Há um total de %d horas disponíveis por dia para estudo.
                                      Retorne apenas um objeto JSON, sem texto explicativo.
                                        {
                                          "totalWeeks": 3,
                                          "dailyStudyHours": %d,
                                          "schedule": [
                                            {
                                              "week": 1,
                                              "days": [
                                                {
                                                  "day": "Monday",
                                                  "studyTopics": [
                                                    {
                                                      "topicName": "Introduction to Programming",
                                                      "difficulty": "Easy",
                                                      "allocatedHours": 2
                                                    },
                                                    {
                                                      "topicName": "Data Structures",
                                                      "difficulty": "Medium",
                                                      "allocatedHours": 2
                                                    }
                                                  ]
                                                },
                                                {
                                                  "day": "Tuesday",
                                                  "studyTopics": [
                                                    {
                                                      "topicName": "Algorithms",
                                                      "difficulty": "Hard",
                                                      "allocatedHours": 4
                                                    }
                                                  ]
                                                }
                                              ]
                                            },
                                            {
                                              "week": 2,
                                              "days": [ ... ]
                                            },
                                            {
                                              "week": 3,
                                              "days": [ ... ]
                                            }
                                          ]
                                        }
                                        """, request.studyTimePerDay(),request.studyTimePerDay());

        FileEntity fileEntity = fileRepository.findById(request.fileId()).orElseThrow(() -> new RuntimeException("Arquivo Não encontrado!"));
        return openAIService.getChatCompletionWithFile(fileEntity, prompt);

    }
}


