package ru.qa.megagenerator.aiAssistant.inners;

import ru.qa.megagenerator.aiAssistant.clients.dto.ollama.OllamaRelease;
import ru.qa.megagenerator.aiAssistant.enums.DownloadFileState;

public class OllamaReleaseItem {
    private String current;
    private DownloadFileState state;
    private String newVersion;
    private OllamaRelease release;

    public String getCurrent() {
        return current;
    }

    public OllamaReleaseItem setCurrent(String current) {
        this.current = current;
        return this;
    }

    public DownloadFileState getState() {
        return state;
    }

    public OllamaReleaseItem setState(DownloadFileState state) {
        this.state = state;
        return this;
    }

    public String getNewVersion() {
        return newVersion;
    }

    public OllamaReleaseItem setNewVersion(String newVersion) {
        this.newVersion = newVersion;
        return this;
    }

    public OllamaRelease getRelease() {
        return release;
    }

    public OllamaReleaseItem setRelease(OllamaRelease release) {
        this.release = release;
        return this;
    }
}
