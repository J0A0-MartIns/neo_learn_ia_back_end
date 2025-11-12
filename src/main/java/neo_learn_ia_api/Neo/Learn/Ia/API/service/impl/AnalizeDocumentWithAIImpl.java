package neo_learn_ia_api.Neo.Learn.Ia.API.service.impl;


import com.fasterxml.jackson.databind.JsonNode;
import neo_learn_ia_api.Neo.Learn.Ia.API.dto.MultipleChoiceQuizResponse;
import neo_learn_ia_api.Neo.Learn.Ia.API.dto.ScheduleRequest;
import neo_learn_ia_api.Neo.Learn.Ia.API.model.FileEntity;
import neo_learn_ia_api.Neo.Learn.Ia.API.model.StudySchedule;
import neo_learn_ia_api.Neo.Learn.Ia.API.repository.FileRepository;
import neo_learn_ia_api.Neo.Learn.Ia.API.repository.StudyProjectRepository;
import neo_learn_ia_api.Neo.Learn.Ia.API.repository.StudyScheduleRepository;
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
import reactor.core.scheduler.Schedulers;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;

@Service
@AllArgsConstructor
public class AnalizeDocumentWithAIImpl implements AnalizeDocumentWithAI {

    private final OpenAIService openAIService;
    private final MultipleChoiceQuestionRepository repository;
    private final FileRepository fileRepository;
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final StudyScheduleRepository studyScheduleRepository;
    private final StudyProjectRepository studyProjectRepository;
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
                                new com.fasterxml.jackson.core.type.TypeReference<List<MultipleChoiceQuestionEntity>>() {
                                }
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

    public Mono<StudySchedule> createScheduleWithFile(ScheduleRequest request) throws IOException {
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
                """, request.studyTimePerDay(), request.studyTimePerDay());

        FileEntity fileEntity = fileRepository.findById(request.fileId())
                .orElseThrow(() -> new RuntimeException("Arquivo não encontrado!"));

        var project = studyProjectRepository.findById(request.studyProjectId())
                .orElseThrow(() -> new RuntimeException("Projeto não encontrado!"));

        return openAIService.getChatCompletionWithFile(fileEntity, prompt)
                .doOnNext(responseJson -> logger.info("Resposta do OpenAI recebida. tamanho={} chars", responseJson == null ? 0 : responseJson.length()))
                .flatMap(responseJson -> {
                    if (responseJson == null || responseJson.isBlank()) {
                        logger.error("Resposta do OpenAI vazia para fileId={} projectId={}", request.fileId(), request.studyProjectId());
                        return Mono.error(new RuntimeException("Resposta do OpenAI vazia"));
                    }

                    try {
                        JsonNode root = objectMapper.readTree(responseJson);

                        // logs dos campos mais importantes se existirem
                        if (root.has("totalWeeks")) {
                            logger.debug("JSON totalWeeks = {}", root.get("totalWeeks").asInt());
                        } else {
                            logger.warn("JSON retornado não contém 'totalWeeks'");
                        }

                        if (root.has("dailyStudyHours")) {
                            logger.debug("JSON dailyStudyHours = {}", root.get("dailyStudyHours").asInt());
                        } else {
                            logger.warn("JSON retornado não contém 'dailyStudyHours'");
                        }

                        Map<String, Object> jsonMap = objectMapper.convertValue(root, Map.class);

                        StudySchedule plan = StudySchedule.builder()
                                .project(project)
                                .scheduleData(jsonMap)
                                .build();

                        logger.info("Preparando para salvar StudySchedule - projectId={}, possibleTopicsCount={}",
                                project.getId(),
                                root.path("schedule").size());

                        return Mono.fromCallable(() -> {
                                    StudySchedule saved = studyScheduleRepository.save(plan);
                                    logger.info("StudySchedule salvo com sucesso - id={}, projectId={}", saved.getId(), project.getId());
                                    return saved;
                                })
                                .subscribeOn(Schedulers.boundedElastic())
                                .doOnError(err -> logger.error("Erro ao salvar StudySchedule no repositório", err));
                    } catch (Exception e) {
                        logger.error("Erro ao processar JSON retornado da API. resposta (parte)={}", safePreview(responseJson), e);
                        return Mono.error(new RuntimeException("Erro ao processar JSON retornado da API", e));
                    }
                })
                .doOnError(e -> logger.error("Fluxo createScheduleWithFile falhou para projectId={}, fileId={}", request.studyProjectId(), request.fileId(), e));
    }

    private String safePreview(String s) {
        if (s == null) return "null";
        int max = 1000;
        return s.length() <= max ? s : s.substring(0, max) + "...(truncated, length=" + s.length() + ")";
    }


}



