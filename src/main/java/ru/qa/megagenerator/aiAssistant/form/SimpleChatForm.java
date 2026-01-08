package ru.qa.megagenerator.aiAssistant.form;

import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.project.Project;
import ru.qa.megagenerator.aiAssistant.clients.dto.ollama.OllamaRelease;
import ru.qa.megagenerator.aiAssistant.items.MessageListPanel;
import ru.qa.megagenerator.aiAssistant.services.ollama.OllamaReleaseService;
import ru.qa.megagenerator.aiAssistant.utils.form.ContextMenuUtils;
import ru.qa.megagenerator.aiAssistant.utils.form.TextAreaUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.stream.Collectors;

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
    private JButton buttonTestStart;
    private Project project;
    private MessageListPanel messagesPanel;
    private JScrollPane messagesScrollPane;
    private Editor editor;


    public SimpleChatForm(Project project) {
        this.project = project;
        TextAreaUtils.setUserAreaHotKeys(inputArea, root, this::onEnterPressed);
        setButtons();
        modifyChatPanel();
        ContextMenuUtils.createPopupMenu(inputArea);
        buttonTestStart.addActionListener(e -> {
            System.out.println("Доступные версии: \n" + OllamaReleaseService.getReleases().stream()
                    .map(OllamaRelease::getTagName).collect(Collectors.joining("\n")));
            System.out.println("Версии на компе: \n" + String.join("\n",
                    OllamaReleaseService.getDownloadedVersions()));

//            if(!systemField.getText().isBlank()) {
//                DeepSeekClient client = ApplicationManager.getApplication().getService(DeepSeekClient.class);
//                String content = client.chat(new AiRequest(systemField.getText(), Map.of("temperature", 0.1)))
//                        .content();
//                System.out.println(content);
//            } else {
//                System.out.println("Строка пуста!");
//            }

        });
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
        messagesPanel.addMessage(text, isUser, project);
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
