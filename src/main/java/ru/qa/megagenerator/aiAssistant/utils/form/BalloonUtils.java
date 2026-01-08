package ru.qa.megagenerator.aiAssistant.utils.form;

import com.intellij.openapi.ui.popup.Balloon;
import com.intellij.openapi.ui.popup.JBPopupFactory;
import com.intellij.openapi.wm.IdeFrame;
import com.intellij.openapi.wm.WindowManager;
import com.intellij.ui.JBColor;
import com.intellij.ui.awt.RelativePoint;
import com.intellij.util.ui.JBUI;
import ru.qa.megagenerator.aiAssistant.enums.BalloonType;

import javax.swing.*;

public final class BalloonUtils {

    public static void show(String text, BalloonType type) {
        JLabel label = new JLabel(text);
        label.setBorder(JBUI.Borders.empty(8));

        IdeFrame frame = WindowManager.getInstance().getIdeFrame(null);
        if (frame == null) {
            return;
        }

        JComponent component = frame.getComponent().getRootPane();

        JBPopupFactory.getInstance()
                .createBalloonBuilder(label)
                .setFillColor(JBColor.PanelBackground)
                .setBorderColor(type.getBorder())
                .setHideOnClickOutside(true)
                .setHideOnKeyOutside(true)
                .setCloseButtonEnabled(true)
                .createBalloon()
                .show(RelativePoint.getSouthEastOf(component), Balloon.Position.atRight);
    }

}
