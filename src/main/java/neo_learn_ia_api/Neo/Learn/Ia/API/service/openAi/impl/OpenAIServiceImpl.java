package neo_learn_ia_api.Neo.Learn.Ia.API.service.openAi.impl;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.module.jsonSchema.JsonSchema;
import com.fasterxml.jackson.module.jsonSchema.JsonSchemaGenerator;
import neo_learn_ia_api.Neo.Learn.Ia.API.config.OpenAiProperties;
import neo_learn_ia_api.Neo.Learn.Ia.API.dto.ChatRequest;
import neo_learn_ia_api.Neo.Learn.Ia.API.dto.ChatResponse;
import neo_learn_ia_api.Neo.Learn.Ia.API.enums.JsonResponseFormat;
import neo_learn_ia_api.Neo.Learn.Ia.API.model.FileEntity;
import neo_learn_ia_api.Neo.Learn.Ia.API.service.openAi.OpenAIService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

    private static final Logger log = LoggerFactory.getLogger(OpenAIServiceImpl.class);
    private final WebClient webClient;
    private final OpenAiProperties properties;

    public OpenAIServiceImpl(WebClient openAIWebClient, OpenAiProperties properties) {
        this.webClient = openAIWebClient;
        this.properties = properties;
    }


    public Mono<String> getChatCompletion(String prompt) {
        ChatRequest request = new ChatRequest(
                properties.getModel(),
                List.of(new ChatRequest.Message("user", prompt))
        );

        return webClient.post()
                .uri(properties.getChatEndpoint())
                .bodyValue(request)
                .retrieve()
                .bodyToMono(ChatResponse.class)
                .map(resp -> resp.choices().get(0).message().content());
    }


    private String uploadFileGeneric(byte[] data, String fileName, String contentType) throws IOException {
        ByteArrayResource resource = new ByteArrayResource(data) {
            @Override
            public String getFilename() {
                return fileName;
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

        String fileId = response.get("id").asText();
        log.info("Arquivo enviado com sucesso para OpenAI. file_id={}", fileId);
        return fileId;
    }

    private String uploadFile(MultipartFile file) throws IOException {
        return uploadFileGeneric(file.getBytes(), file.getOriginalFilename(), file.getContentType());
    }


    private String uploadFile(FileEntity fileEntity) throws IOException {
        return uploadFileGeneric(fileEntity.getData(), fileEntity.getFileName(), fileEntity.getFileType());
    }

    public Mono<String> getChatCompletionWithFile(MultipartFile file, String prompt) throws IOException {
        String fileId = uploadFile(file);
        return sendChatWithFile(fileId, prompt);
    }


    public Mono<String> getChatCompletionWithFile(FileEntity fileEntity, String prompt) throws IOException {
        String fileId = uploadFile(fileEntity);
        return sendChatWithFile(fileId, prompt);
    }

    private Mono<String> sendChatWithFile(String fileId, String prompt) {
        Map<String, Object> messageContent = new HashMap<>();
        messageContent.put("role", "user");
        messageContent.put("content", List.of(
                Map.of("type", "input_text", "text", prompt),
                Map.of("type", "input_file", "file_id", fileId)
        ));

        Map<String, Object> requestPayload = new HashMap<>();
        requestPayload.put("model", properties.getModel());
        requestPayload.put("input", List.of(messageContent));

        return webClient.post()
                .uri(properties.getResponsesEndpoint())
                .header("Authorization", "Bearer " + properties.getKey())
                .bodyValue(requestPayload)
                .retrieve()
                .bodyToMono(JsonNode.class)
                .map(resp -> resp.get("output").get(0).get("content").get(0).get("text").asText());
    }

}
