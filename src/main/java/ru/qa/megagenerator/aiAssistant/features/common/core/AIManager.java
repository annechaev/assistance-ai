package ru.qa.megagenerator.aiAssistant.features.common.core;

import ru.qa.megagenerator.aiAssistant.features.common.interfaces.AIProvider;

public class AIManager {
    private static AIProvider activeProvider;

    public static void setProvider(AIProvider provider) {
        activeProvider = provider;
    }

    public static AIProvider getProvider() {
        return activeProvider;
    }
}
