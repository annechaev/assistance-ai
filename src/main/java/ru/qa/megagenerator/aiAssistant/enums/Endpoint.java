package ru.qa.megagenerator.aiAssistant.enums;

public enum Endpoint {
    OLLAMA_API_BASE("https://api.github.com/repos/ollama"),
    OLLAMA_BASE("https://github.com/ollama"),
    OLLAMA_RELEASE(OLLAMA_API_BASE.getUrl() + "/ollama/releases"),
    OLLAMA_LATEST_RELEASE(OLLAMA_API_BASE.getUrl() + "/ollama/releases/latest"),
    OLLAMA_DOWNLOAD_RELEASE(OLLAMA_BASE.getUrl() + "/ollama/releases/download/%s/%s");

    private String url;

    Endpoint(String url) {
        this.url = url;
    }

    public String getUrl() {
        return url;
    }
}
