package ru.qa.megagenerator.aiAssistant.utils.form;

import com.intellij.ide.highlighter.HighlighterFactory;
import com.intellij.openapi.Disposable;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.editor.EditorFactory;
import com.intellij.openapi.editor.ex.EditorEx;
import com.intellij.openapi.editor.highlighter.EditorHighlighter;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.Disposer;

public class EditorUtils {

    public static EditorEx createEditor(String codeText, Project project, Disposable parentDisposable) {
        Document document = EditorFactory.getInstance().createDocument(codeText);
        EditorEx editor = (EditorEx) EditorFactory.getInstance().createViewer(document, project);
        editor.getSettings().setUseSoftWraps(true);
        editor.getSettings().setAdditionalLinesCount(0);
        editor.getSettings().setAdditionalColumnsCount(0);
        editor.getComponent().setOpaque(false);

        // Подсветка Java
        EditorHighlighter highlighter = HighlighterFactory.createHighlighter(project, "Dummy.java");
        editor.setHighlighter(highlighter);
        Disposable editorDisposable = () -> {
            if (!editor.isDisposed()) {
                EditorFactory.getInstance().releaseEditor(editor);
            }
        };
        Disposer.register(parentDisposable, editorDisposable);

        return editor;
    }

}
