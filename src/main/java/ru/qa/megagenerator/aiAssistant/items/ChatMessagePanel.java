package ru.qa.megagenerator.aiAssistant.items;

import com.intellij.icons.AllIcons;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.fileEditor.FileEditorManager;
import com.intellij.openapi.project.Project;
import com.intellij.ui.EditorTextField;
import ru.qa.megagenerator.aiAssistant.utils.form.BubbleUtils;
import ru.qa.megagenerator.aiAssistant.utils.form.EditorUtils;
import ru.qa.megagenerator.aiAssistant.utils.form.PsiUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.datatransfer.StringSelection;
import java.util.ArrayList;
import java.util.List;


public class ChatMessagePanel extends JPanel {

    public static List<JPanel> getRows(String messageText, boolean isUser, Project project, int newWidth) {
        List<BubbleMessageItem> messageItems = BubbleUtils.getBubbleMessages(messageText, "code");
        Editor editor = FileEditorManager.getInstance(project).getSelectedTextEditor();

        List<JPanel> allBlocks = new ArrayList<>();
        messageItems.forEach(item -> {

            JPanel block = item.getIsCode()
                    ? createCodeBubble(item.getMessage(), project)
                    : BubbleUtils.createTextBubble(item.getMessage(), isUser);
            block.setBorder(BorderFactory.createLineBorder(Color.BLUE));
            JPanel rowContainer = new JPanel();
            rowContainer.setBorder(BorderFactory.createLineBorder(Color.gray));
            rowContainer.setLayout(new BoxLayout(rowContainer, BoxLayout.X_AXIS));
            rowContainer.setOpaque(false);
            rowContainer.setMaximumSize(new Dimension(Integer.MAX_VALUE, Integer.MAX_VALUE));
            SwingUtilities.invokeLater(() -> {
                block.setMaximumSize(new Dimension(newWidth, Integer.MAX_VALUE));
                block.revalidate();
            });
            if(isUser) {
                rowContainer.add(Box.createHorizontalGlue());
                rowContainer.add(block);
            } else {
                rowContainer.add(block);
                rowContainer.add(Box.createHorizontalGlue());
            }

            allBlocks.add(rowContainer);
        });
        return allBlocks;
    }

    private static JPanel createCodeBubble(String codeText, Project project) {
        JPanel wrapper = new JPanel(new BorderLayout());
        wrapper.setOpaque(false);
        JPanel codePanel = BubbleUtils.createBubblePanel(new Color(0xF5F5F5));
        // === Панель для editor ===
        JPanel editorWrapper = new JPanel(new BorderLayout());
        editorWrapper.setOpaque(false);
        EditorTextField editorTextField = EditorUtils.createJavaEditor(project, codeText);
        editorWrapper.add(editorTextField, BorderLayout.CENTER);
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
