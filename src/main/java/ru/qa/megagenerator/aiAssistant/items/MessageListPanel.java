package ru.qa.megagenerator.aiAssistant.items;

import javax.swing.*;
import java.awt.*;

public class MessageListPanel extends JPanel implements Scrollable {

    public MessageListPanel() {
        super();
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
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