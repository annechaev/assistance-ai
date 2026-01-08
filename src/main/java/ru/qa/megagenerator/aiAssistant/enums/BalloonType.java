package ru.qa.megagenerator.aiAssistant.enums;

import com.intellij.ui.JBColor;

public enum BalloonType {

    INFO(JBColor.GRAY), WARNING(JBColor.YELLOW), ERROR(JBColor.RED), SUCCESS(JBColor.GREEN);

    private JBColor border;

    BalloonType(JBColor border) {
        this.border = border;
    }

    public JBColor getBorder() {
        return border;
    }
}
