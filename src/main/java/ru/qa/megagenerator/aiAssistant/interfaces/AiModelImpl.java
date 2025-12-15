package ru.qa.megagenerator.aiAssistant.interfaces;

import ru.qa.megagenerator.aiAssistant.enums.AiModelType;

/**
 * Implementations MUST be registered as IntelliJ services.
 */
public interface AiModelImpl {

    String id();
    boolean isLocal();
    boolean isRemote();
    String formName();
    AiModelType type();
}
