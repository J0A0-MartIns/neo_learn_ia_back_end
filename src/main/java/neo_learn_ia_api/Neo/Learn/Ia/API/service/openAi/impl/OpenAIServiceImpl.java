package neo_learn_ia_api.Neo.Learn.Ia.API.service.openAi.impl;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.module.jsonSchema.JsonSchema;
import com.fasterxml.jackson.module.jsonSchema.JsonSchemaGenerator;
import neo_learn_ia_api.Neo.Learn.Ia.API.config.OpenAiProperties;
import neo_learn_ia_api.Neo.Learn.Ia.API.dto.ChatRequest;
import neo_learn_ia_api.Neo.Learn.Ia.API.dto.ChatResponse;
import neo_learn_ia_api.Neo.Learn.Ia.API.enums.JsonResponseFormat;
import neo_learn_ia_api.Neo.Learn.Ia.API.service.openAi.OpenAIService;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.io.IOException;
import java.util.*;

@Service
public class OpenAIServiceImpl implements OpenAIService {

    private final WebClient webClient;
    private final OpenAiProperties properties;
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final JsonSchemaGenerator schemaGenerator;

    public OpenAIServiceImpl(WebClient openAIWebClient, OpenAiProperties properties) {
        this.webClient = openAIWebClient;
        this.properties = properties;
        this.schemaGenerator = new JsonSchemaGenerator(objectMapper);
    }

    public Mono<String> getChatCompletion(String prompt) {
        ChatRequest request = new ChatRequest(
                "gpt-4-mini",
                List.of(new ChatRequest.Message("user", prompt))
        );

        return webClient.post()
                .uri(properties.getChatEndpoint())
                .bodyValue(request)
                .retrieve()
                .bodyToMono(ChatResponse.class)
                .map(resp -> resp.choices().get(0).message().content());
    }

    public <T> Mono<T> analyzeFiles(
            List<MultipartFile> files,
            String instruction,
            JsonResponseFormat format,
            Class<T> responseType
    ) throws IOException {

        List<String> fileIds = new ArrayList<>();
        for (MultipartFile file : files) {
            fileIds.add(uploadFile(file));
        }

        Map<String, Object> body = new HashMap<>();
        body.put("model", "gpt-4.1-mini");
        body.put("input", instruction);
        body.put("file_ids", fileIds);

        switch (format) {
            case JSON -> body.put("response_format", Map.of("type", "json"));
            case JSON_SCHEMA -> body.put("response_format", Map.of(
                    "type", "json_schema",
                    "json_schema", Map.of(
                            "name", "custom_schema",
                            "schema", generateJsonSchema(responseType)
                    )
            ));
            default -> {}
        }

        return webClient.post()
                .uri(properties.getResponsesEndpoint())
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(body)
                .retrieve()
                .bodyToMono(JsonNode.class)
                .map(json -> {
                    JsonNode output = json.get("output").get(0).get("content").get(0).get("text");
                    if (format == JsonResponseFormat.TEXT) {
                        return (T) output.asText();
                    }
                    try {
                        return objectMapper.readValue(output.toString(), responseType);
                    } catch (Exception e) {
                        throw new RuntimeException("Erro ao converter resposta JSON", e);
                    }
                });
    }

    private String uploadFile(MultipartFile file) throws IOException {
        ByteArrayResource resource = new ByteArrayResource(file.getBytes()) {
            @Override
            public String getFilename() {
                return file.getOriginalFilename();
            }
        };

        JsonNode response = webClient.post()
                .uri(properties.getFilesEndpoint())
                .contentType(MediaType.MULTIPART_FORM_DATA)
                .body(BodyInserters.fromMultipartData("file", resource)
                        .with("purpose", "assistants"))
                .retrieve()
                .bodyToMono(JsonNode.class)
                .block();

        return response.get("id").asText();
    }

    private Map<String, Object> generateJsonSchema(Class<?> clazz) {
        try {
            JsonSchema schema = schemaGenerator.generateSchema(clazz);
            return objectMapper.convertValue(schema, Map.class);
        } catch (Exception e) {
            throw new RuntimeException("Erro ao gerar JSON Schema para " + clazz.getSimpleName(), e);
        }
    }
}
