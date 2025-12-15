package ru.qa.megagenerator.aiAssistant.clients;

import com.jetbrains.Service;
import ru.qa.megagenerator.aiAssistant.aiModels.DeepSeekModel;
import ru.qa.megagenerator.aiAssistant.clients.dto.AiRequest;
import ru.qa.megagenerator.aiAssistant.clients.dto.AiResponse;
import ru.qa.megagenerator.aiAssistant.exceptions.AiException;
import ru.qa.megagenerator.aiAssistant.interfaces.AiClient;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Map;

import static com.google.common.net.HttpHeaders.CONTENT_TYPE;
import static com.intellij.microservices.mime.MimeTypes.APPLICATION_JSON;
import static ru.qa.megagenerator.aiAssistant.utils.common.FileUtils.getContentFromFile;

@Service
public class DeepSeekClient implements AiClient {

    private final HttpClient httpClient = HttpClient.newHttpClient();
    private final String REQUEST_BODY_PATH = "dto/deepseek_request_body.json";

    @Override
    public String modelId() {
        return DeepSeekModel.ID;
    }

    @Override
    public AiResponse chat(AiRequest request) {
        try {
            System.out.println("Начали!");
            HttpRequest httpRequest = HttpRequest.newBuilder()
                    .uri(URI.create("http://localhost:11434/v1/chat/completions"))
                    .header(CONTENT_TYPE, APPLICATION_JSON)
                    .POST(HttpRequest.BodyPublishers.ofString(buildBody(request)))
                    .build();
            HttpResponse<String> response = httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString());
            System.out.println("Закончили!");
            return parseResponse(response.body());
        } catch (Exception e) {
            throw new AiException("Запрос в %s прошел упал. ".formatted(modelId()), e);
        }
    }

    private String buildBody(AiRequest request) {
        // Jackson / Gson
        return getContentFromFile(REQUEST_BODY_PATH).formatted(request.prompt());
    }

    private AiResponse parseResponse(String body) {
        // JSON parsing
        return new AiResponse(body, Map.of());
    }
}
