package ru.qa.megagenerator.aiAssistant.utils.form;

import javax.swing.*;

public final class RenderDescriptor {

    private final String text;
    private final Icon icon;
    private final String tooltip;

    public RenderDescriptor(String text, Icon icon, String tooltip) {
        this.text = text;
        this.icon = icon;
        this.tooltip = tooltip;
    }

    public String text() { return text; }
    public Icon icon() { return icon; }
    public String tooltip() { return tooltip; }
}
