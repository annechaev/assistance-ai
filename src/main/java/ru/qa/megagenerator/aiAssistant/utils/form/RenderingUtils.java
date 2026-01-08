package ru.qa.megagenerator.aiAssistant.utils.form;

import ru.qa.megagenerator.aiAssistant.clients.dto.ollama.OllamaRelease;
import ru.qa.megagenerator.aiAssistant.interfaces.AiModelImpl;

import javax.swing.*;
import java.awt.*;
import java.util.function.Function;

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

    public static DefaultListCellRenderer getOllamaReleaseRendering() {
        return new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                JLabel label = new JLabel();
                if(value != null) {
                    if(value instanceof OllamaRelease item) {
                        label.setText(item.getTagName());
                    } else {
                        label.setText(value.toString());
                    }
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

    public static <T> DefaultListCellRenderer renderer(Function<T, RenderDescriptor> mapper) {
        return new DefaultListCellRenderer() {

            @Override
            public Component getListCellRendererComponent(
                    JList<?> list,
                    Object value,
                    int index,
                    boolean isSelected,
                    boolean cellHasFocus
            ) {
                JLabel label = (JLabel) super.getListCellRendererComponent(
                        list, value, index, isSelected, cellHasFocus
                );

                if (value != null) {
                    try {
                        RenderDescriptor rd = mapper.apply((T) value);
                        label.setText(rd.text());
                        label.setIcon(rd.icon());
                        label.setToolTipText(rd.tooltip());
                    } catch (ClassCastException e) {
                        label.setText(value.toString());
                        label.setIcon(null);
                        label.setToolTipText(null);
                    }
                }

                return label;
            }
        };
    }

}
