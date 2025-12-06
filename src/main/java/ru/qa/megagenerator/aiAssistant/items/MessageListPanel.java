package ru.qa.megagenerator.aiAssistant.items;

import com.intellij.openapi.project.Project;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.util.List;

public class MessageListPanel extends JPanel implements Scrollable {

    public MessageListPanel() {
        super();
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                int maxWidth = (int)(getWidth() * 0.8);
                for (Component row : getComponents()) {
                    if (!(row instanceof JPanel rowContainer)) continue;
                    if (rowContainer.getComponentCount() == 0) continue;
                    Component block = rowContainer.getComponent(0);
                    block.setMaximumSize(new Dimension(maxWidth, Integer.MAX_VALUE));
                    block.revalidate();
                }
                revalidate();
                repaint();
            }
        });
    }

    public void addMessage(String messageText, boolean isUser, Project project) {
        int maxWidth = (int)(getWidth() * 0.8);
        List<JPanel> rows = ChatMessagePanel.getRows(messageText, isUser, this, project, maxWidth);
        rows.forEach(row -> {
            add(row);
            add(Box.createVerticalStrut(5));
        });
    }

    @Override
    public Dimension getPreferredScrollableViewportSize() {
        // пусть scrollpane решает сам
        return getPreferredSize();
    }

    @Override
    public int getScrollableUnitIncrement(Rectangle visibleRect, int orientation, int direction) {
        // высота маленького шага прокрутки
        return 16; // стандарт
    }

    @Override
    public int getScrollableBlockIncrement(Rectangle visibleRect, int orientation, int direction) {
        // один "экран"
        return visibleRect.height;
    }

    @Override
    public boolean getScrollableTracksViewportWidth() {
        // ширину подстраиваем под viewport — так делают мессенджеры
        return true;
    }

    @Override
    public boolean getScrollableTracksViewportHeight() {
        // ВАЖНО: не растягивать messagesPanel по высоте
        return false;
    }
}