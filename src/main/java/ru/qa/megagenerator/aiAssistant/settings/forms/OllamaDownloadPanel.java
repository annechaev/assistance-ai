package ru.qa.megagenerator.aiAssistant.settings.forms;

import com.intellij.icons.AllIcons;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.progress.ProgressIndicator;
import ru.qa.megagenerator.aiAssistant.clients.dto.ollama.OllamaRelease;
import ru.qa.megagenerator.aiAssistant.enums.DownloadFileState;
import ru.qa.megagenerator.aiAssistant.inners.OllamaReleaseItem;
import ru.qa.megagenerator.aiAssistant.interfaces.ConfigurableFormInterface;
import ru.qa.megagenerator.aiAssistant.interfaces.OllamaReleaseListener;
import ru.qa.megagenerator.aiAssistant.services.ollama.OllamaReleaseService;
import ru.qa.megagenerator.aiAssistant.services.ollama.OllamaState;

import javax.swing.*;

import static ru.qa.megagenerator.aiAssistant.enums.DownloadFileState.*;
import static ru.qa.megagenerator.aiAssistant.services.ollama.OllamaReleaseService.resolveStatusMessage;
import static ru.qa.megagenerator.aiAssistant.services.ollama.OllamaService.createStructure;

public class OllamaDownloadPanel extends JPanel implements ConfigurableFormInterface {
    private JPanel root;
    private JPanel downloadPanel;
    private JProgressBar downloadProgressBar;
    private JButton downloadButton;
    private JButton updateButton;
    private JButton removeButton;
    private JLabel downloadVersionLabel;
    private JLabel downloadInfoLabel;

    private OllamaRelease lastRelease;

    public OllamaDownloadPanel() {
        createStructure();
        translate();
        setButtonActions();
        setFields();
        accessible(null);
    }

    public void translate() {
        downloadButton.setIcon(AllIcons.Actions.Download);
        updateButton.setIcon(AllIcons.General.ArrowUp);
        removeButton.setIcon(AllIcons.General.Remove);
    }

    public void setFields() {
        OllamaState state = ApplicationManager.getApplication().getService(OllamaState.class);
        downloadVersionLabel.setText(state.getCurrentRelease());
        OllamaReleaseService service = ApplicationManager.getApplication().getService(OllamaReleaseService.class);
        service.setListener(new OllamaReleaseListener() {
            @Override
            public void onReleasesUpdateSuccess(OllamaReleaseItem release) {
                lastRelease = release.getRelease();
                accessible(release.getState());
                downloadVersionLabel.setText(release.getCurrent());
            }

            @Override
            public void onReleaseUpdateFailed(Throwable error) {
                error.printStackTrace();
            }
        });
        service.asyncLatestReleaseIfNeed();
    }

    public void setButtonActions() {
        OllamaReleaseService service = ApplicationManager.getApplication().getService(OllamaReleaseService.class);
        downloadButton.addActionListener(e -> {
            accessible(DOWNLOADING);
            service.downloadRelease(lastRelease,  downloadProgressBar, "Download ollama %s");
        });
        updateButton.addActionListener(e -> {
            accessible(DOWNLOADING);
            service.downloadRelease(lastRelease,  downloadProgressBar, "Download ollama %s");
        });
        removeButton.addActionListener(e -> {
            ProgressIndicator indicator = service.getCurrentIndicator();
            if (indicator != null) {
                indicator.cancel();
            }
            OllamaReleaseService.delete();
            accessible(NOT_INSTALLED);
        });
    }

    public void save() {

    }

    @Override
    public boolean isModified() {
        return false;
    }

    @Override
    public JComponent rootPane() {
        return root;
    }

    public void accessible(DownloadFileState state) {
        if(state == null) {
            downloadButton.setEnabled(false);
            updateButton.setEnabled(false);
            removeButton.setEnabled(false);
            downloadProgressBar.setVisible(false);
            downloadProgressBar.setIndeterminate(true);
        } else {
            downloadButton.setEnabled(state.getConfig().download());
            updateButton.setEnabled(state.getConfig().update());
            removeButton.setEnabled(state.getConfig().remove());
            downloadProgressBar.setVisible(state.getConfig().progress());

            String message = resolveStatusMessage(
                    state,
                    "Ollama",
                    downloadVersionLabel.getText(),
                    lastRelease != null ? lastRelease.getTagName() : null,
                    lastRelease != null ? lastRelease.calculateSize() : null
            );
            downloadInfoLabel.setText(message);
        }
    }
}
