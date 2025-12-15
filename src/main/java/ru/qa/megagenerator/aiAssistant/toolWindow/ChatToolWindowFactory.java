package ru.qa.megagenerator.aiAssistant.toolWindow;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.wm.ToolWindow;
import com.intellij.openapi.wm.ToolWindowFactory;
import com.intellij.ui.content.Content;
import com.intellij.ui.content.ContentFactory;
import org.jetbrains.annotations.NotNull;
import ru.qa.megagenerator.aiAssistant.form.SimpleChatForm;

public class ChatToolWindowFactory implements ToolWindowFactory {

    @Override
    public void createToolWindowContent(@NotNull Project project, @NotNull ToolWindow toolWindow) {
        SimpleChatForm chatPanel = new SimpleChatForm(project);
        ContentFactory contentFactory = ContentFactory.getInstance();
        Content content = contentFactory.createContent(chatPanel.rootPanel(), "", false);
        toolWindow.getContentManager().addContent(content);
    }
}
