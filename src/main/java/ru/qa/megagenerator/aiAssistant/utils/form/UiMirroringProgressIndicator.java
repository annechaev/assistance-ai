package ru.qa.megagenerator.aiAssistant.utils.form;

import com.intellij.ide.util.DelegatingProgressIndicator;
import com.intellij.openapi.progress.ProgressIndicator;
import groovyjarjarantlr4.v4.runtime.misc.NotNull;
import ru.qa.megagenerator.aiAssistant.clients.dto.ollama.OllamaRelease;

import javax.swing.*;

public class UiMirroringProgressIndicator extends DelegatingProgressIndicator {

    private JProgressBar progressBar;
    private OllamaRelease release;

    public UiMirroringProgressIndicator(
            @NotNull ProgressIndicator original,
            @NotNull JProgressBar progressBar,
            @NotNull OllamaRelease release
    ) {
        super(original);
        this.progressBar = progressBar;
        this.release = release;
    }

    public OllamaRelease getRelease() {
        return release;
    }

    public void rebind(JProgressBar progressBar, JButton statusLabel) {
        this.progressBar = progressBar;
        this.progressBar.setIndeterminate(true);
    }

    public void refresh() {
        this.progressBar.setValue(0);
    }
}
