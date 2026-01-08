package ru.qa.megagenerator.aiAssistant.services;

import com.intellij.openapi.components.Service;
import ru.qa.megagenerator.aiAssistant.clients.dto.ollama.OllamaRelease;
import ru.qa.megagenerator.aiAssistant.utils.common.FileUtils;

@Service
public final class MockService {

    public OllamaRelease getNewRelease() {
        return FileUtils.getFromResources("inner/mock/ollama-new-release.json", OllamaRelease.class);
    }

}
