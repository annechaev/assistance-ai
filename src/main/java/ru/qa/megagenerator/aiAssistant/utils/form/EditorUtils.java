package ru.qa.megagenerator.aiAssistant.utils.form;

import com.intellij.openapi.fileTypes.FileType;
import com.intellij.openapi.fileTypes.FileTypeManager;
import com.intellij.openapi.project.Project;
import com.intellij.ui.EditorTextField;

public class EditorUtils {

    public static EditorTextField createJavaEditor(Project project, String initialText) {
        FileType javaFileType = FileTypeManager.getInstance().getFileTypeByExtension("java");

        if (javaFileType == null) {
            javaFileType = FileTypeManager.getInstance().getStdFileType(initialText);
        }
        EditorTextField editorTextField = new EditorTextField(initialText, project, javaFileType);
        editorTextField.setOneLineMode(false); // Многострочный режим

        return editorTextField;
    }

}
