package ru.qa.megagenerator.aiAssistant.utils.form;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;

public class ButtonUtils {

    public static JButton createFadedIconButton(Icon icon, String toolTip) {
        final int PREFERRED_SIZE = 20;
        JButton btn = new JButton();
        btn.setToolTipText(toolTip);
        btn.setBorderPainted(false);
        btn.setContentAreaFilled(false);
        btn.setFocusPainted(false);
        btn.setOpaque(false);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btn.setPreferredSize(new Dimension(PREFERRED_SIZE, PREFERRED_SIZE));
        btn.setMaximumSize(new Dimension(PREFERRED_SIZE, PREFERRED_SIZE));

        Icon faded = makeIconTransparent(icon, 0.55f);   // приглушённый
        Icon full = makeIconTransparent(icon, 1.0f);    // яркий

        btn.setIcon(faded);

        btn.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                btn.setIcon(full);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                btn.setIcon(faded);
            }
        });

        return btn;
    }

    public static void addClickBackground(JButton btn) {
        btn.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                btn.setOpaque(true);
                btn.setContentAreaFilled(true);
                btn.setBackground(new Color(0x3D8AFF));
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                btn.setOpaque(false);
                btn.setContentAreaFilled(false);
                btn.setBackground(null);
            }
        });
    }

    private static Icon makeIconTransparent(Icon icon, float alpha) {
        int w = icon.getIconWidth();
        int h = icon.getIconHeight();
        BufferedImage img = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);

        Graphics2D g2 = img.createGraphics();
        icon.paintIcon(null, g2, 0, 0);
        g2.dispose();

        for (int y = 0; y < h; y++) {
            for (int x = 0; x < w; x++) {
                int rgba = img.getRGB(x, y);
                Color c = new Color(rgba, true);
                img.setRGB(x, y, new Color(
                        c.getRed(),
                        c.getGreen(),
                        c.getBlue(),
                        (int)(c.getAlpha() * alpha)
                ).getRGB());
            }
        }
        return new ImageIcon(img);
    }

}
