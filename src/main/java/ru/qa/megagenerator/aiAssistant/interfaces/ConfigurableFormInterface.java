package ru.qa.megagenerator.aiAssistant.interfaces;

import javax.swing.*;

public interface ConfigurableFormInterface {

    void save();
    boolean isModified();
    JComponent rootPane();

}
