package ru.qa.megagenerator.aiAssistant.utils.form;

import com.intellij.openapi.command.WriteCommandAction;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.ScrollType;
import com.intellij.openapi.fileEditor.FileEditorManager;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiDocumentManager;
import com.intellij.psi.PsiFile;
import com.intellij.psi.codeStyle.CodeStyleManager;

public class PsiUtils {

    public static void writeToCaret(String codeText, Project  project) {
        if(project != null) {
            Editor mainEditor = FileEditorManager.getInstance(project).getSelectedTextEditor();
            Document targetDoc = mainEditor.getDocument();
            PsiFile psiFile = PsiDocumentManager.getInstance(project).getPsiFile(targetDoc);

            Runnable runnable = () -> {
                // 1. Текущая позиция курсора
                int caretOffset = mainEditor.getCaretModel().getOffset();

                // 2. Вставляем текст
                targetDoc.insertString(caretOffset, codeText);

                int start = caretOffset;
                int end = caretOffset + codeText.length();

                // 3. Коммитим PSI
                PsiDocumentManager.getInstance(project).commitDocument(targetDoc);

                // 4. Форматируем ТОЛЬКО вставленную область
                CodeStyleManager.getInstance(project).reformatText(psiFile, start, end);

                // 5. После форматирования caret может сдвинуться,
                // поэтому теперь нужно вычислить новое смещение
                int newCaretOffset = Math.min(end, targetDoc.getTextLength());

                // 6. Перемещаем caret в новую позицию
                mainEditor.getCaretModel().moveToOffset(newCaretOffset);
                mainEditor.getScrollingModel().scrollToCaret(ScrollType.MAKE_VISIBLE);
            };

            WriteCommandAction.runWriteCommandAction(project, runnable);
        }
    }

}
