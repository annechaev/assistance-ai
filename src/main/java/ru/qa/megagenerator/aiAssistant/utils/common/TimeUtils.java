package ru.qa.megagenerator.aiAssistant.utils.common;

import ru.qa.megagenerator.aiAssistant.clients.dto.ollama.CacheOllamaRelease;

import java.time.Duration;
import java.time.Instant;

public class TimeUtils {

    public static boolean isExpired(CacheOllamaRelease cache, Duration ttl) {
        return cache.getFetchedAt()
                .plus(ttl)
                .isBefore(Instant.now());
    }

}
