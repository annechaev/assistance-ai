package ru.qa.megagenerator.aiAssistant.interfaces;

import ru.qa.megagenerator.aiAssistant.clients.dto.AiRequest;
import ru.qa.megagenerator.aiAssistant.clients.dto.AiResponse;
import ru.qa.megagenerator.aiAssistant.exceptions.AiException;

public interface AiClient {

    String modelId();

    AiResponse chat(AiRequest request) throws AiException;
}