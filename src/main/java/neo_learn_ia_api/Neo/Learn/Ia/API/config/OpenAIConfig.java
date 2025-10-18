package neo_learn_ia_api.Neo.Learn.Ia.API.config;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class OpenAIConfig {

    private final OpenAiProperties properties;

    public OpenAIConfig(OpenAiProperties properties) {
        this.properties = properties;
    }

    @Bean
    public WebClient openAIWebClient(WebClient.Builder builder) {
        return builder
                .baseUrl(properties.getBaseUrl())
                .defaultHeader("Content-Type", "application/json")
                .defaultHeader("Authorization", "Bearer " + properties.getKey())
                .build();
    }
}