package ru.qa.megagenerator.aiAssistant.settings.forms;

import ru.qa.megagenerator.aiAssistant.interfaces.ConfigurableFormInterface;
import ru.qa.megagenerator.aiAssistant.utils.form.FormUtils;

import javax.swing.*;

public class OllamaForm implements ConfigurableFormInterface {
    private JPanel root;
    private JLabel separatorLabel;
    private JScrollPane downloadScrollPane;
    private JScrollPane loadScrollPane;

    private OllamaDownloadPanel ollamaDownloadPanel;
    private OllamaLoadPanel ollamaLoadPanel;

    public OllamaForm() {
        ollamaDownloadPanel = new OllamaDownloadPanel();
        ollamaLoadPanel = new OllamaLoadPanel();
        addComponents();
        translate();
    }

    public void translate() {
        FormUtils.setPanelBorderWithTitle(downloadScrollPane, "Download");
        FormUtils.setPanelBorderWithTitle(loadScrollPane, "Load");
    }

    public void addComponents() {
        downloadScrollPane.setViewportView(ollamaDownloadPanel.rootPane());
        loadScrollPane.setViewportView(ollamaLoadPanel.rootPane());
    }

    @Override
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
}
