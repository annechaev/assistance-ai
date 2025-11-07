package ru.qa.megagenerator.aiAssistant.clients.deepSeek;

import com.fasterxml.jackson.databind.ObjectMapper;
import ru.qa.megagenerator.aiAssistant.clients.deepSeek.dto.DeepSeekRequest;
import ru.qa.megagenerator.aiAssistant.clients.deepSeek.dto.DeepSeekResponse;
import ru.qa.megagenerator.aiAssistant.features.common.interfaces.AIProvider;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.time.Duration;

public class DeepSeekClient implements AIProvider {

    private static final String API_URL = "https://api.deepseek.com/v1/chat/completions";

    private final String apiKey;
    private final HttpClient client;
    private final ObjectMapper mapper = new ObjectMapper();

    public DeepSeekClient(String apiKey) {
        this.apiKey = apiKey;
        this.client = HttpClient.newBuilder()
                .connectTimeout(Duration.ofSeconds(20))
                .build();
    }

    @Override
    public String send(String prompt) {
        try {
            DeepSeekRequest requestBody = new DeepSeekRequest("deepseek-chat", prompt);

            String body = mapper.writeValueAsString(requestBody);

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(API_URL))
                    .header("Content-Type", "application/json")
                    .header("Authorization", "Bearer " + apiKey)
                    .POST(HttpRequest.BodyPublishers.ofString(body, StandardCharsets.UTF_8))
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() != 200) {
                throw new RuntimeException("DeepSeek API error: " + response.statusCode() + " → " + response.body());
            }

            DeepSeekResponse deepSeekResponse = mapper.readValue(response.body(), DeepSeekResponse.class);
            return deepSeekResponse.getMessageContent();

        } catch (Exception e) {
            throw new RuntimeException("Failed to call DeepSeek API", e);
        }
    }

    @Override
    public String getName() {
        return "";
    }

    @Override
    public String generateCode(String prompt) {
        return "";
    }

    @Override
    public String optimizeCode(String source) {
        return "";
    }

    @Override
    public String explainCode(String source) {
        return "";
    }
}
