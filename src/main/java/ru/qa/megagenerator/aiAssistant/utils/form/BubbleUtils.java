package ru.qa.megagenerator.aiAssistant.utils.form;

import ru.qa.megagenerator.aiAssistant.enums.MessageBubbleOrientationType;
import ru.qa.megagenerator.aiAssistant.items.BubbleMessageItem;


import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.text.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

import static ru.qa.megagenerator.aiAssistant.utils.form.ButtonUtils.createFadedIconButton;

public class BubbleUtils {

    private static final int ARC = 16;

    /**
     * Разбивается сообщение на элементы (код и пользовательские сообщения)
     * @param text
     * @param tag
     * @return
     */
    public static List<BubbleMessageItem> getBubbleMessages(String text, String tag){
        List<BubbleMessageItem> list = new ArrayList<>();

        int index = 0;
        while (index < text.length()) {
            int start = text.indexOf("<%s>".formatted(tag), index);
            if (start == -1) {
                list.add(new BubbleMessageItem(text.substring(index).trim(), false));
                break;
            }
            if (start > index) {
                list.add(new BubbleMessageItem(text.substring(index, start).trim(), false));
            }
            int end = text.indexOf("</%s>".formatted(tag), start);
            if (end == -1) end = text.length();
            list.add(new BubbleMessageItem(text.substring(start + 6, end).trim(), true));
            index = end + 7;
        }

        return list;
    }

    public static JTextPane createTextPane(String text, boolean isUser) {
        JTextPane textPane = new JTextPane();
        textPane.setEditable(false);
        textPane.setOpaque(false);
        textPane.setBorder(new EmptyBorder(8, 12, 8, 12));
        textPane.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        textPane.setEditorKit(new WrapEditorKit());

        StyledDocument doc = textPane.getStyledDocument();
        SimpleAttributeSet attr = new SimpleAttributeSet();
        StyleConstants.setForeground(attr, isUser ? Color.BLACK : Color.DARK_GRAY);
        StyleConstants.setLineSpacing(attr, 0.1f);
        try {
            doc.insertString(doc.getLength(), text, attr);
        } catch (BadLocationException e) {
            e.printStackTrace();
        }
        return textPane;
    }

    public static JPanel createTextBubble(String text, boolean isUser) {
        JPanel panel = createBubblePanel(isUser ? new Color(0xD1EDFF) : new Color(0xEAEAEA));
        JTextPane textPane = BubbleUtils.createTextPane(text, isUser);
        panel.add(textPane, BorderLayout.CENTER);
        return panel;
    }

    private static class WrapEditorKit extends StyledEditorKit {
        private ViewFactory defaultFactory = new WrapEditorKit.WrapColumnFactory();

        @Override
        public ViewFactory getViewFactory() {
            return defaultFactory;
        }

        static class WrapColumnFactory implements ViewFactory {
            public View create(Element elem) {
                String kind = elem.getName();
                if (kind != null) {
                    return switch (kind) {
                        case AbstractDocument.ContentElementName -> new WrapEditorKit.WrapLabelView(elem);
                        case AbstractDocument.ParagraphElementName -> new ParagraphView(elem);
                        case AbstractDocument.SectionElementName -> new BoxView(elem, View.Y_AXIS);
                        case StyleConstants.ComponentElementName -> new ComponentView(elem);
                        case StyleConstants.IconElementName -> new IconView(elem);
                        default -> new LabelView(elem);
                    };
                }
                return new LabelView(elem);
            }
        }

        static class WrapLabelView extends LabelView {
            public WrapLabelView(Element elem) {
                super(elem);
            }

            @Override
            public float getMinimumSpan(int axis) {
                return 0;
            }
        }
    }

    public static void messageBubbleOrientation(JPanel container, JPanel block, MessageBubbleOrientationType type) {
        switch (type) {
                case LEFT:
                    container.add(block);
                    container.add(Box.createHorizontalGlue());
                    break;
                case RIGHT:
                    container.add(Box.createHorizontalGlue());
                    container.add(block);
                    break;
            default:
                break;
        }
    }

    public static JPanel createBubblePanel(Color color) {
        return new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(color);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), ARC, ARC);
                g2.dispose();
            }

            @Override
            public boolean isOpaque() {
                return false;
            }
        };
    }

    public static JButton createToolBubbleButton(String text, Icon icon, Consumer<ActionEvent> consumer) {
        JButton btn = createFadedIconButton(icon, text);
        btn.setOpaque(false);

        btn.addActionListener(consumer::accept);
        btn.setBackground(null);
        ButtonUtils.addClickBackground(btn);
        return btn;
    }

}
