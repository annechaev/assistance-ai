package ru.qa.megagenerator.aiAssistant.old;

import java.util.List;
import java.util.Map;

public class DeepSeekRequest {

    public String model;
    public List<Map<String, String>> messages;

    public DeepSeekRequest(String model, String prompt) {
        this.model = model;
        this.messages = List.of(
                Map.of("role", "user", "content", prompt)
        );
    }

}
