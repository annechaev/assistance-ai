package ru.qa.megagenerator.aiAssistant.utils.form;

import javax.swing.*;

public class ContextMenuUtils {

    public static void createPopupMenu(JTextArea area) {
        JPopupMenu menu = new JPopupMenu();

        JMenuItem cut = new JMenuItem("Cut");
        cut.addActionListener(e -> area.cut());

        JMenuItem copy = new JMenuItem("Copy");
        copy.addActionListener(e -> area.copy());

        JMenuItem paste = new JMenuItem("Paste");
        paste.addActionListener(e -> area.paste());

        JMenuItem selectAll = new JMenuItem("Select All");
        selectAll.addActionListener(e -> area.selectAll());

        menu.add(cut);
        menu.add(copy);
        menu.add(paste);
        menu.addSeparator();
        menu.add(selectAll);
        area.setComponentPopupMenu(menu);
    }

}
