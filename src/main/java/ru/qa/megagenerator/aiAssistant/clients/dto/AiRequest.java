package ru.qa.megagenerator.aiAssistant.clients.dto;

import java.util.Map;

public record AiRequest(
        String prompt,
        Map<String, Object> parameters
) {}