package ru.qa.megagenerator.aiAssistant.interfaces;

import ru.qa.megagenerator.aiAssistant.utils.form.RenderDescriptor;

import javax.swing.*;

public abstract class RendererAbstract {

    public abstract String formName();
    public abstract Icon getIcon();

    public RenderDescriptor render() {
        return new RenderDescriptor(formName(), getIcon(), null);
    }

}
