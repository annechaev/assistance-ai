package ru.qa.megagenerator.aiAssistant.settings.forms;

import com.intellij.icons.AllIcons;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.project.ProjectManager;
import com.intellij.ui.JBColor;
import com.intellij.ui.components.JBTextField;
import ru.qa.megagenerator.aiAssistant.enums.FileType;
import ru.qa.megagenerator.aiAssistant.inners.ValidationError;
import ru.qa.megagenerator.aiAssistant.services.ollama.OllamaReleaseService;
import ru.qa.megagenerator.aiAssistant.services.ollama.OllamaState;
import ru.qa.megagenerator.aiAssistant.utils.common.FileUtils;
import ru.qa.megagenerator.aiAssistant.utils.form.FormUtils;
import ru.qa.megagenerator.aiAssistant.utils.form.ValidationUiUtil;

import javax.swing.*;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

import static ru.qa.megagenerator.aiAssistant.services.ollama.OllamaReleaseService.getCpuArchString;
import static ru.qa.megagenerator.aiAssistant.services.ollama.OllamaService.VERSION_PATTERN;

public class OllamaLoadPanel extends JPanel {
    private JPanel root;
    private JPanel loadPanel;
    private JLabel versionLabel;
    private JButton chooseFileButton;
    private JBTextField versionField;
    private JButton loadButton;
    private JLabel loadInfoLabel;

    public OllamaLoadPanel() {
        versionField.getEmptyText().setText("v0.0.0");
        translate();
        addButtonActions();
    }

    public JPanel rootPane() {
        return root;
    }

    public void translate() {
        loadButton.setIcon(AllIcons.Actions.Upload);
        loadInfoLabel.setText("Ollama for Linux need" +  getCpuArchString());
        loadInfoLabel.setForeground(JBColor.ORANGE);
    }

    public void addButtonActions() {
        OllamaState state = ApplicationManager.getApplication().getService(OllamaState.class);
        FormUtils.bindChooser(
                chooseFileButton,
                ProjectManager.getInstance().getOpenProjects()[0],
                state::getLoadPath,
                state::setLoadPath,
                rootPane(),
                FileType.FILE,
                "Choose File",
                FileUtils.extensionFilter("tgz"));
        versionField.addFocusListener(new FocusAdapter() {

            @Override
            public void focusGained(FocusEvent e) {
                ValidationUiUtil.clear(versionField);
            }
        });

        loadButton.addActionListener(e -> {
            if(checkFields()) {
                OllamaReleaseService service = ApplicationManager.getApplication().getService(OllamaReleaseService.class);
                service.moveRelease(versionField.getText(), state.getLoadPath());
            }
        });
    }

    public boolean checkFields() {
        List<ValidationError> errors = validateFields();
        if(!errors.isEmpty()) {
            FormUtils.showErrors(errors);
            return false;
        }
        return true;
    }

    private List<ValidationError> validateFields() {
        OllamaState state = ApplicationManager.getApplication().getService(OllamaState.class);
        List<ValidationError> errors = new ArrayList<>();
        String version = versionField.getText().trim();
        if(!version.matches(VERSION_PATTERN)) {
            errors.add(new ValidationError(versionField, "Version number must be like v0.0.1"));
        }
        if(!Files.exists(state.getLoadPath())) {
            errors.add(new ValidationError(chooseFileButton, "Load File Not Found"));
        }
        return errors;
    }
}
