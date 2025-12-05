package ru.qa.megagenerator.aiAssistant.form;

import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.project.Project;
import ru.qa.megagenerator.aiAssistant.items.MessageListPanel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import ru.qa.megagenerator.aiAssistant.items.ChatMessagePanel;
import ru.qa.megagenerator.aiAssistant.utils.form.ContextMenuUtils;
import ru.qa.megagenerator.aiAssistant.utils.form.TextAreaUtils;

public class SimpleChatForm extends JPanel {
    private JPanel root;
    private JPanel content;
    private JPanel inputPanel;
    private JScrollPane inputScrollPane;
    private JTextArea inputArea;
    private JLabel inputLabel;
    private JButton sendButton;
    private JPanel textPanel;
    private JTextField systemField;
    private JButton systemButton;
    private JLabel systemLabel;
    private Project project;
    private JPanel messagesPanel;
    private JScrollPane messagesScrollPane;
    private Editor editor;


    public SimpleChatForm(Project project) {
        this.project = project;
        TextAreaUtils.setUserAreaHotKeys(inputArea, root, this::onEnterPressed);
        setButtons();
        modifyChatPanel();
        ContextMenuUtils.createPopupMenu(inputArea);
    }

    public JComponent rootPanel() {
        return root;
    }

    public void onEnterPressed() {
        addMessage(inputArea.getText(), true);
        inputArea.setText("");
    }

    private void addMessage(String text, boolean isUser) {
        if(text.isBlank()) return;
        ChatMessagePanel messagePanel = new ChatMessagePanel(text, isUser, messagesPanel, project);
        messagesPanel.add(messagePanel);
        messagesPanel.add(Box.createVerticalStrut(5));
        messagesPanel.revalidate();
        messagesPanel.repaint();
        SwingUtilities.invokeLater(() -> {
            JScrollBar bar = messagesScrollPane.getVerticalScrollBar();
            bar.setValue(bar.getMaximum());
        });
    }

    private void setButtons() {
        sendButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                onEnterPressed();
            }
        });
        systemButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                addMessage(systemField.getText(), false);
            }
        });
    }

    private void modifyChatPanel() {
        messagesPanel = new MessageListPanel();
        messagesScrollPane = new JScrollPane(messagesPanel);
        content.add(messagesScrollPane, BorderLayout.CENTER);
    }
}
