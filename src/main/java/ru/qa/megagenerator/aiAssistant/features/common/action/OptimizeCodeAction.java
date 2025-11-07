package ru.qa.megagenerator.aiAssistant.features.common.action;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.CommonDataKeys;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.Messages;
import org.jetbrains.annotations.NotNull;
import ru.qa.megagenerator.aiAssistant.toolWindows.optimize.OptimizeToolWindowPanel;

public class OptimizeCodeAction extends AnAction {

    public OptimizeCodeAction() {
        super("Оптимизировать код");
    }

    @Override
    public void actionPerformed(@NotNull AnActionEvent e) {
        System.out.println("Перешли в акшн");
        Project project = e.getProject();
        Editor editor = e.getData(CommonDataKeys.EDITOR);

        if (project == null || editor == null) return;

        String selectedText = editor.getSelectionModel().getSelectedText();
        if (selectedText == null || selectedText.isBlank()) {
            Messages.showWarningDialog("Выделите код для оптимизации.", "Нет выделения");
            return;
        }

        // передаём выделенный код в ToolWindow
        OptimizeToolWindowPanel.show(project, selectedText);
    }
}
