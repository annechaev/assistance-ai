package ru.qa.megagenerator.aiAssistant.toolWindows.optimize;

import ru.qa.megagenerator.aiAssistant.clients.deepSeek.DeepSeekClient;
import ru.qa.megagenerator.aiAssistant.enums.OptimizationType;
import ru.qa.megagenerator.aiAssistant.features.common.interfaces.AIProvider;

public class CodeOptimizer {

    public static String optimize(String code, OptimizationType type) {
        // Здесь подключается ChatGPT или DeepSeek API
        String prompt = switch (type) {
            case SIMPLIFY -> "Упрости код, сохранив функциональность:\n" + code;
            case PERFORMANCE -> "Оптимизируй код по производительности:\n" + code;
            case ANALYSIS -> "Проанализируй код и предложи улучшения:\n" + code;
        };

        AIProvider provider = new DeepSeekClient("sk-8dd1f650eac6426d9fe3c23e27b91540");

        return provider.send(prompt);
    }
}
