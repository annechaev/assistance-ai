package ru.qa.megagenerator.aiAssistant.utils.form;

import ru.qa.megagenerator.aiAssistant.interfaces.AiModelImpl;

import javax.swing.*;
import java.awt.*;

public final class RenderingUtils {

    public static DefaultListCellRenderer getModelRendering() {
        return new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                JLabel label = new JLabel();
                if(value != null) {
                    AiModelImpl item = (AiModelImpl) value;
                    label.setText(item.formName());
                    if (isSelected) {
                        label.setBackground(list.getSelectionBackground());
                        label.setForeground(list.getSelectionForeground());
                    } else {
                        label.setBackground(list.getBackground());
                        label.setForeground(list.getForeground());
                    }
                }
                return label;
            }
        };
    }

}
