package ru.qa.megagenerator.aiAssistant.utils.form;

import com.intellij.ide.highlighter.HighlighterFactory;
import com.intellij.openapi.Disposable;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.editor.EditorFactory;
import com.intellij.openapi.editor.ex.EditorEx;
import com.intellij.openapi.editor.highlighter.EditorHighlighter;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.Disposer;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

public class EditorUtils {

    public static EditorEx createEditor(String codeText, Project project) {
        Document document = EditorFactory.getInstance().createDocument(codeText);
        EditorEx editor = (EditorEx) EditorFactory.getInstance().createViewer(document, project);
        editor.getSettings().setUseSoftWraps(true);
        editor.getSettings().setAdditionalLinesCount(0);
        editor.getSettings().setAdditionalColumnsCount(0);
        editor.getComponent().setOpaque(false);

        // Подсветка Java
        EditorHighlighter highlighter = HighlighterFactory.createHighlighter(project, "Dummy.java");
        editor.setHighlighter(highlighter);

        return editor;
    }

    public static void addResizeListener(JPanel editorWrapper, String codeText, Project project) {
        // disposable, к которому будем привязывать редакторы
        Disposable wrapperDisposable = Disposer.newDisposable("editor-wrapper");
        Disposer.register(project, wrapperDisposable);

        final int[] lastWidth = {editorWrapper.getWidth()};
        final EditorEx[] editorRef = new EditorEx[1];
        final Disposable[] editorDisposableRef = new Disposable[1];

        editorWrapper.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                SwingUtilities.invokeLater(() -> {
                    int width = editorWrapper.getWidth();
                    if (width == lastWidth[0]) return;
                    lastWidth[0] = width;

                    // --- dispose old editor properly ---
                    if (editorRef[0] != null) {
                        // если мы регистрировали disposable — удаляем его
                        if (editorDisposableRef[0] != null) {
                            // dispose the registered disposable (this will call releaseEditor)
                            Disposer.dispose(editorDisposableRef[0]);
                            editorDisposableRef[0] = null;
                        } else {
                            // на всякий случай: fallback напрямую releaseEditor
                            EditorFactory.getInstance().releaseEditor(editorRef[0]);
                        }
                        editorRef[0] = null;
                    }

                    editorWrapper.removeAll();

                    // --- create new editor ---
                    EditorEx newEditor = EditorUtils.createEditor(codeText, project);
                    editorRef[0] = newEditor;
                    editorWrapper.add(newEditor.getComponent(), BorderLayout.CENTER);

                    // создаём disposable, который при dispose() освободит редактор
                    Disposable edDisp = new Disposable() {
                        @Override
                        public void dispose() {
                            // защитимся на случай повторного вызова
                            if (editorRef[0] != null && !editorRef[0].isDisposed()) {
                                EditorFactory.getInstance().releaseEditor(editorRef[0]);
                            } else {
                                // если редактор уже не в editorRef (например, заменён), всё равно попробуем release
                                try {
                                    EditorFactory.getInstance().releaseEditor(newEditor);
                                } catch (Exception ignore) {
                                }
                            }
                        }
                    };

                    // регистрируем edDisp как child(wrapperDisposable)
                    Disposer.register(wrapperDisposable, edDisp);
                    editorDisposableRef[0] = edDisp;

                    editorWrapper.revalidate();
                    editorWrapper.repaint();
                });
            }
        });
    }

}
