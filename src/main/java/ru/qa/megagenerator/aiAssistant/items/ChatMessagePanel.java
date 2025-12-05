package ru.qa.megagenerator.aiAssistant.items;

import com.intellij.icons.AllIcons;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.ex.EditorEx;
import com.intellij.openapi.fileEditor.FileEditorManager;
import com.intellij.openapi.project.Project;
import ru.qa.megagenerator.aiAssistant.enums.MessageBubbleOrientationType;
import ru.qa.megagenerator.aiAssistant.utils.form.BubbleUtils;
import ru.qa.megagenerator.aiAssistant.utils.form.EditorUtils;
import ru.qa.megagenerator.aiAssistant.utils.form.PsiUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.datatransfer.StringSelection;
import java.util.ArrayList;
import java.util.List;


public class ChatMessagePanel extends JPanel {

    public ChatMessagePanel(String messageText, boolean isUser, JComponent parentPanel, Project project) {
        setLayout(new BorderLayout());
        setOpaque(false);

        List<BubbleMessageItem> messageItems = BubbleUtils.getBubbleMessages(messageText, "code");

        // Вертикальный контейнер для всех блоков сообщения
        JPanel vertical = new JPanel();
        vertical.setLayout(new BoxLayout(vertical, BoxLayout.Y_AXIS));
        vertical.setOpaque(false);
        Editor editor = FileEditorManager.getInstance(project).getSelectedTextEditor();

        List<JPanel> allBlocks = new ArrayList<>();

        messageItems.forEach(item -> {

            JPanel block = item.getIsCode()
                    ? createCodeBubble(item.getMessage(), project, editor, parentPanel)
                    : BubbleUtils.createTextBubble(item.getMessage(), isUser);

            JPanel horizontalContainer = new JPanel();
            horizontalContainer.setLayout(new BoxLayout(horizontalContainer, BoxLayout.X_AXIS));
            horizontalContainer.setOpaque(false);
            BubbleUtils.messageBubbleOrientation(horizontalContainer, block, isUser
                    ? MessageBubbleOrientationType.RIGHT : MessageBubbleOrientationType.LEFT);

            vertical.add(horizontalContainer);
            vertical.add(Box.createVerticalStrut(5));

            allBlocks.add(block);
        });
        add(vertical, BorderLayout.CENTER);

        // Ограничение ширины каждого блока 80% от родителя
        parentPanel.addComponentListener(new java.awt.event.ComponentAdapter() {
            @Override
            public void componentResized(java.awt.event.ComponentEvent e) {
                int maxWidth = (int) (parentPanel.getWidth() * 0.8);
                for (JPanel block : allBlocks) {
                    block.setMaximumSize(new Dimension(maxWidth, Integer.MAX_VALUE));
                    block.revalidate();
                }
            }
        });

        SwingUtilities.invokeLater(() -> {
            int maxWidth = (int) (parentPanel.getWidth() * 0.8);
            for (JPanel block : allBlocks) {
                block.setMaximumSize(new Dimension(maxWidth, Integer.MAX_VALUE));
            }
        });
    }

    private JPanel createCodeBubble(String codeText, Project project, Editor editor, JComponent parentPanel) {
        JPanel wrapper = new JPanel(new BorderLayout());
        wrapper.setOpaque(false);
        JPanel codePanel = BubbleUtils.createBubblePanel(new Color(0xF5F5F5));
        // === Панель для editor ===
        JPanel editorWrapper = new JPanel(new BorderLayout());
        editorWrapper.setOpaque(false);
        EditorEx editorEx = EditorUtils.createEditor(codeText, project);
        editorWrapper.add(editorEx.getComponent(), BorderLayout.CENTER);
        EditorUtils.addResizeListener(editorWrapper, codeText, project);
        codePanel.add(editorWrapper, BorderLayout.CENTER);

        // === Панель слева для кнопок ===
        JPanel buttonsPanel = new JPanel();
        buttonsPanel.setLayout(new BoxLayout(buttonsPanel, BoxLayout.Y_AXIS));

        JButton insertButton = BubbleUtils.createToolBubbleButton("Вставить", AllIcons.Json.Object,
                e -> PsiUtils.writeToCaret(codeText, project));
        JButton copyButton = BubbleUtils.createToolBubbleButton("Копировать", AllIcons.Actions.Copy,
                e -> {
                    StringSelection selection = new StringSelection(codeText);
                    Toolkit.getDefaultToolkit().getSystemClipboard().setContents(selection, null);
                });

        // Добавляем кнопку в панель
        buttonsPanel.add(Box.createVerticalStrut(6));
        buttonsPanel.add(insertButton);
        buttonsPanel.add(Box.createVerticalStrut(4));
        buttonsPanel.add(copyButton);
        buttonsPanel.add(Box.createVerticalGlue());

        buttonsPanel.setMaximumSize(new Dimension(30, Integer.MAX_VALUE));

        codePanel.add(buttonsPanel, BorderLayout.WEST);

        wrapper.add(codePanel, BorderLayout.CENTER);
        return wrapper;
    }
}
