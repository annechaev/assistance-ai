package ru.qa.megagenerator.aiAssistant.settings.forms;

import ru.qa.megagenerator.aiAssistant.interfaces.AiModelImpl;
import ru.qa.megagenerator.aiAssistant.interfaces.ConfigurableFormInterface;
import ru.qa.megagenerator.aiAssistant.settings.states.ModelState;
import ru.qa.megagenerator.aiAssistant.utils.form.RenderingUtils;

import javax.swing.*;
import java.util.List;

import static ru.qa.megagenerator.aiAssistant.constants.CommonConstants.*;

public class ModelForm implements ConfigurableFormInterface {
    private JPanel root;
    private JPanel content;
    private JPanel remotePanel;
    private JPanel localPanel;
    private JRadioButton remoteRadioButton;
    private JRadioButton localRadioButton;
    private JPanel remoteConfigPanel;
    private JPanel localConfigPanel;
    private JComboBox remoteModelTypeBox;
    private JTextField remoteUrlField;
    private JTextField remoteTokenField;
    private JComboBox localModelTypeBox;
    private JButton initModelButton;
    private JLabel remoteModelTypeLabel;
    private JLabel remoteUrlLabel;
    private JLabel remoteTokenLabel;
    private JLabel localModeTypeLabel;
    private JLabel clueLabel;

    private List<JRadioButton> modelTypeButtons = List.of(remoteRadioButton, localRadioButton);
    private List<JComponent> remoteComponents
            = List.of(remoteModelTypeBox, remoteUrlField, remoteTokenField);
    private List<JComponent> localComponents
            = List.of(localModelTypeBox, initModelButton);

    public ModelForm() {
        remoteModelTypeBox.setModel(new DefaultComboBoxModel<>(getRemoteModels().toArray()));
        remoteModelTypeBox.setRenderer(RenderingUtils.getModelRendering());
        localModelTypeBox.setModel(new DefaultComboBoxModel<>(getLocalModels().toArray()));
        localModelTypeBox.setRenderer(RenderingUtils.getModelRendering());

        ButtonGroup group = new ButtonGroup();
        modelTypeButtons.forEach(button -> {
            group.add(button);
            button.addActionListener(e -> accessible());
        });
        setFields();
    }

    private void setFields() {
        ModelState state = ModelState.getInstance();
        if(state != null) {
            remoteRadioButton.setSelected(state.isRemote());
            localRadioButton.setSelected(state.isLocal());
            remoteUrlField.setText(state.getRemoteUrl());
            remoteTokenField.setText(state.getRemoteToken());
            localModelTypeBox.setSelectedItem(state.getLocalModel());
            remoteModelTypeBox.setSelectedItem(state.getRemoteModel());
            accessible();
        }
    }

    private void saveFields() {
        ModelState state = ModelState.getInstance();
        if(state != null) {
            state.setIsRemote(remoteRadioButton.isSelected());
            state.setIsLocal(localRadioButton.isSelected());
            state.setRemoteUrl(remoteUrlField.getText());
            state.setRemoteToken(remoteTokenField.getText());
            state.setLocalModel((AiModelImpl) localModelTypeBox.getSelectedItem());
            state.setRemoteModel((AiModelImpl) remoteModelTypeBox.getSelectedItem());
        }
    }

    private void accessible() {
        remoteComponents.forEach(component -> component.setEnabled(remoteRadioButton.isSelected()));
        localComponents.forEach(component -> component.setEnabled(localRadioButton.isSelected()));
    }

    @Override
    public void save() {
        saveFields();
    }

    @Override
    public boolean isModified() {
        ModelState state = ModelState.getInstance();
        if(state != null) {
            return state.isRemote() != remoteRadioButton.isSelected()
                    || state.isLocal() != localRadioButton.isSelected()
                    || state.getLocalModel() != localModelTypeBox.getSelectedItem()
                    || state.getRemoteModel() != remoteModelTypeBox.getSelectedItem()
                    || !state.getRemoteUrl().equals(remoteUrlField.getText())
                    || !state.getRemoteToken().equals(remoteTokenField.getText());
        }
        return false;
    }

    @Override
    public JComponent rootPane() {
        return root;
    }
}
