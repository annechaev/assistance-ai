package ru.qa.megagenerator.aiAssistant.utils.form;

import com.intellij.ui.JBColor;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.LineBorder;

public final class ValidationUiUtil {

    private static final String BORDER_KEY = "validation.border";

    public static void markInvalid(JComponent c) {
        if (c.getClientProperty(BORDER_KEY) == null) {
            c.putClientProperty(BORDER_KEY, c.getBorder());
        }
        c.setBorder(new LineBorder(JBColor.RED, 2));
    }

    public static void clear(JComponent c) {
        Object border = c.getClientProperty(BORDER_KEY);
        if (border instanceof Border b) {
            c.setBorder(b);
        }
        c.putClientProperty(BORDER_KEY, null);
    }
}