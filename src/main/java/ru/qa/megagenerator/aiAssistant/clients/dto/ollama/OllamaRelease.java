package ru.qa.megagenerator.aiAssistant.clients.dto.ollama;

import com.fasterxml.jackson.annotation.*;
import ru.qa.megagenerator.aiAssistant.enums.DownloadFileState;
import ru.qa.megagenerator.aiAssistant.interfaces.RendererAbstract;
import ru.qa.megagenerator.aiAssistant.utils.common.FileUtils;
import ru.qa.megagenerator.aiAssistant.utils.form.RenderDescriptor;

import javax.swing.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import static ru.qa.megagenerator.aiAssistant.services.ollama.OllamaService.ollamaHome;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonAutoDetect(
        fieldVisibility = JsonAutoDetect.Visibility.ANY, // Читать/писать в любые поля
        getterVisibility = JsonAutoDetect.Visibility.NONE, // Игнорировать геттеры
        setterVisibility = JsonAutoDetect.Visibility.NONE  // Игнорировать сеттеры
)
public class OllamaRelease extends RendererAbstract {

    @JsonProperty("tag_name")
    private String tagName;
    @JsonProperty("prerelease")
    private boolean preRelease = false;
    @JsonProperty("assets")
    private List<Asset> assets = new ArrayList<>();
    @JsonIgnore
    private DownloadFileState state = DownloadFileState.NOT_INSTALLED;
    @JsonIgnore
    private Float size = 0f;

    public OllamaRelease() {
    }

    @JsonCreator
    public OllamaRelease(@JsonProperty("assets") List<Asset> assets) {
        this.assets = assets;
        this.size = calculateSize();
    }

    @Override
    public String formName() {
        return getTagName();
    }

    @Override
    public Icon getIcon() {
        return null;
    }

    public RenderDescriptor render() {
        return new RenderDescriptor(
                getTagName() + " (%.1f Gb)".formatted(size),
                null,
                "Ollama release " + getTagName()
        );
    }

    @Override
    public String toString() {
        return tagName + " (%.1f Gb)".formatted(size != 0f ? size : calculateSize());
    }

    public String getTagName() { return tagName; }
    public void setTagName(String tagName) { this.tagName = tagName; } // Добавьте сеттер

    public boolean isPreRelease() { return preRelease; }
    public void setPreRelease(boolean preRelease) { this.preRelease = preRelease; }

    public DownloadFileState getState() {
        return state;
    }

    public void setState(DownloadFileState state) {
        this.state = state;
    }

    public List<Asset> getAssets() {
        return assets;
    }

    public void setAssets(List<Asset> assets) {
        this.assets = assets;
    }

    public Float getSize() {
        return size;
    }

    public void setSize(Float size) {
        this.size = size;
    }

    public boolean isDownloading() {
        return FileUtils.getVersionFiles(getTagName()).stream()
                .anyMatch(file -> file.getFileName().toString().endsWith(".part"));
    }

    public boolean isDownloadable() {
        Path versionHome = ollamaHome().resolve(getTagName());
        return !Files.exists(versionHome) || FileUtils.getDirectoryFiles(versionHome).isEmpty();
    }

    public boolean isDownloaded() {
        Path versionHome = ollamaHome().resolve(getTagName());
        return Files.exists(versionHome) && FileUtils.getDirectoryFiles(versionHome).stream().anyMatch(path -> path.getFileName().toString().endsWith(".zip"));
    }

    public Float calculateSize() {
        return assets != null && !assets.isEmpty()
                ? Math.round((assets.stream().mapToDouble(Asset::getSize).sum() / (1024 * 1024 * 1024)) * 10) / 10f
                : 0f;
    }
}
