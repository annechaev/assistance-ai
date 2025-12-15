package ru.qa.megagenerator.aiAssistant.clients.dto;

import java.util.Map;

public record AiResponse(
        String content,
        Map<String, Object> metadata
) {}
