package ru.qa.megagenerator.aiAssistant.interfaces;

import ru.qa.megagenerator.aiAssistant.clients.dto.ollama.CacheOllamaRelease;
import ru.qa.megagenerator.aiAssistant.clients.dto.ollama.OllamaRelease;
import ru.qa.megagenerator.aiAssistant.inners.OllamaReleaseItem;

import java.util.List;

public interface OllamaReleaseListener {
    default void onReleasesUpdated(CacheOllamaRelease releases) {}
    default void onReleaseUpdateFailed(Throwable error) {}
    default void onReleasesUpdateSuccess(OllamaReleaseItem release) {}
}
