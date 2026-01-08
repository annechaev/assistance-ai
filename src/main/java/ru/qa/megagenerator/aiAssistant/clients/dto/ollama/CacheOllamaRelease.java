package ru.qa.megagenerator.aiAssistant.clients.dto.ollama;

import org.jetbrains.annotations.NotNull;

import java.time.Instant;
import java.util.List;

public class CacheOllamaRelease {

    private Instant fetchedAt;
    private List<OllamaRelease> releases;

    public CacheOllamaRelease() {}

    public CacheOllamaRelease(Instant fetchedAt, List<OllamaRelease> releases) {
        this.fetchedAt = fetchedAt;
        this.releases = releases;
    }

    public Instant getFetchedAt() {
        return fetchedAt;
    }

    public List<OllamaRelease> getReleases() {
        return releases;
    }

    @Override
    public @NotNull String toString() {
        return fetchedAt.toString() + ": " + releases;
    }
}
