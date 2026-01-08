package ru.qa.megagenerator.aiAssistant.inners;

import javax.swing.*;

public record ValidationError(
        JComponent component,
        String message
) {}
