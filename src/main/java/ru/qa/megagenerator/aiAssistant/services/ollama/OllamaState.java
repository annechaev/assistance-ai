package ru.qa.megagenerator.aiAssistant.services.ollama;

import com.intellij.openapi.components.PersistentStateComponent;
import com.intellij.openapi.components.Service;
import com.intellij.openapi.components.State;
import com.intellij.openapi.components.Storage;
import com.intellij.util.xmlb.XmlSerializerUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.nio.file.Path;
import java.nio.file.Paths;

@State(name = "ollama update", storages = @Storage("ollama-update.xml"))
@Service
public final class OllamaState implements PersistentStateComponent<OllamaState> {

    public static final String DEFAULT_CURRENT_RELEASE = "?";

    private String currentRelease = DEFAULT_CURRENT_RELEASE;
    private Float currentReleaseSize = 0f;
    private String lastRelease = "?";
    private Float lastReleaseSize = 0f;
    private long lastUpdate = 0;
    private String loadPathString = "";

    public OllamaState() {}

    public String getCurrentRelease() {
        return currentRelease;
    }

    public void setCurrentRelease(String currentRelease) {
        this.currentRelease = currentRelease;
    }

    public String getLastRelease() {
        return lastRelease;
    }

    public void setLastRelease(String lastRelease) {
        this.lastRelease = lastRelease;
    }

    public long getLastUpdate() {
        return lastUpdate;
    }

    public void setLastUpdate(long lastUpdate) {
        this.lastUpdate = lastUpdate;
    }

    public Float getCurrentReleaseSize() {
        return currentReleaseSize;
    }

    public OllamaState setCurrentReleaseSize(Float currentReleaseSize) {
        this.currentReleaseSize = currentReleaseSize;
        return this;
    }

    public Float getLastReleaseSize() {
        return lastReleaseSize;
    }

    public OllamaState setLastReleaseSize(Float lastReleaseSize) {
        this.lastReleaseSize = lastReleaseSize;
        return this;
    }

    public Path getLoadPath() {
        return Paths.get(loadPathString);
    }

    public OllamaState setLoadPath(Path loadPath) {
        this.loadPathString = loadPath.toString();
        return this;
    }

    @Override
    public @Nullable OllamaState getState() {
        return this;
    }

    @Override
    public void loadState(@NotNull OllamaState state) {
        XmlSerializerUtil.copyBean((state), this);
    }
}
