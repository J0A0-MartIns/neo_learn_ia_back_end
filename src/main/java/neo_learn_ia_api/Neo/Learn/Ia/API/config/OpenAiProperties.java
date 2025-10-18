package neo_learn_ia_api.Neo.Learn.Ia.API.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "openai.api")
@Getter
@Setter
public class OpenAiProperties {

    private String baseUrl;
    private String chatEndpoint;
    private String responsesEndpoint;
    private String filesEndpoint;
    private String key;

}
