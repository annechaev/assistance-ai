package ru.qa.megagenerator.aiAssistant.enums;

import com.jetbrains.cef.remote.thrift.annotation.Nullable;
import ru.qa.megagenerator.aiAssistant.inners.StateButtonConfig;

import java.util.Optional;

public enum DownloadFileState {
    NOT_INSTALLED(
            new StateButtonConfig(true, false, false, false),
            "Need to download %s (%.1f Gb)"),
    DOWNLOADING(
            new StateButtonConfig(false, false, true, true),
            "%s is downloading. Please, wait"),    // *.part файл → DOWNLOADING
    INSTALLED(
            new StateButtonConfig(false, false, true, false),
            "%s is downloaded!"),      // архив + распаковка успешна
    FAILED(
            new StateButtonConfig(true, false, false, false),
            null),          // exception
    UPDATABLE(
            new StateButtonConfig(false, true, true, false),
            "New version is available! %s => %s (%.1f Gb)");

    private final StateButtonConfig stateButtonConfig;
    private final @Nullable String message;

    DownloadFileState(StateButtonConfig stateButtonConfig, @Nullable String message) {
        this.stateButtonConfig = stateButtonConfig;
        this.message = message;
    }

    public StateButtonConfig getConfig() {
        return stateButtonConfig;
    }

    public Optional<String> message() {
        return Optional.ofNullable(message);
    }
}
