package ru.qa.megagenerator.aiAssistant.utils.form;

import com.intellij.openapi.fileChooser.FileChooser;
import com.intellij.openapi.fileChooser.FileChooserDescriptor;
import com.intellij.openapi.fileChooser.FileChooserDescriptorFactory;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.Condition;
import com.intellij.openapi.vfs.LocalFileSystem;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.ui.JBColor;
import org.jetbrains.annotations.NotNull;
import ru.qa.megagenerator.aiAssistant.enums.FileType;
import ru.qa.megagenerator.aiAssistant.inners.ValidationError;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.nio.file.Path;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Supplier;

public final class FormUtils {

    private FormUtils() {}

    public static void setPanelBorderWithTitle(JComponent panel, String title) {
        panel.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(JBColor.GRAY),
                title,
                TitledBorder.LEFT,
                TitledBorder.TOP,
                new Font("Arial", Font.BOLD, 14),
                JBColor.GRAY
        ));
    }

    public static void setEnabledRecursively(Component component, boolean enabled) {
        component.setEnabled(enabled);
        if (component instanceof Container container) {
            for (Component child : container.getComponents()) {
                setEnabledRecursively(child, enabled);
            }
        }
    }

    public static void showErrors(List<ValidationError> errors) {
        errors.forEach(error -> {
            ValidationUiUtil.markInvalid(error.component());
            error.component().setToolTipText(error.message());
        });
    }

    public static void clearValidate(List<ValidationError> errors) {
        errors.forEach(error -> ValidationUiUtil.clear(error.component()));
    }

    /**
     * Универсальный биндинг кнопки для выбора файла или папки.
     *
     * @param button кнопка, по нажатию на которую открывается chooser
     * @param project проект IDEA
     * @param getter возвращает текущее значение пути (String)
     * @param setter сохраняет выбранный путь
     * @param root родительский компонент для диалога
     * @param fileType папка или файл
     * @param title заголовок окна выбора
     */
    public static void bindChooser(
            @NotNull JButton button,
            @NotNull Project project,
            @NotNull Supplier<Path> getter,
            @NotNull Consumer<Path> setter,
            JComponent root,
            FileType fileType,
            @NotNull String title,
            Condition<VirtualFile> filter
    ) {
        button.addActionListener(e -> {

            // Определяем начальную папку или файл
            VirtualFile initial = Optional.ofNullable(getter.get())
                    .map(path -> LocalFileSystem.getInstance().findFileByPath(path.toString()))
                    .orElse(project.getBaseDir());

            // Создаём дескриптор: файл или папка
            FileChooserDescriptor descriptor = fileType == FileType.DIRECTORY
                    ? FileChooserDescriptorFactory.createSingleFolderDescriptor()
                    : FileChooserDescriptorFactory.createSingleFileDescriptor();

            descriptor.setTitle(title);
            if(filter != null) descriptor.withFileFilter(filter);

            FileChooser.chooseFile(
                    descriptor,
                    project,
                    root,
                    initial,
                    selected -> {
                        if (selected != null) {
                            setter.accept(Path.of(selected.getPath()));
                            button.setText(selected.getName());
                        }
                    }
            );
        });
    }
}
