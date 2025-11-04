package ru.qa.megagenerator.toolWindows.optimize;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.wm.ToolWindow;
import com.intellij.openapi.wm.ToolWindowManager;
import ru.qa.megagenerator.enums.OptimizationType;

import javax.swing.*;
import java.awt.*;

public class OptimizeToolWindowPanel extends JPanel {

    private final Project project;
    private static OptimizeToolWindowPanel instance;

    private final JTabbedPane tabs = new JTabbedPane();
    private final JTextArea originalArea = new JTextArea();
    private final JTextArea optimizedArea = new JTextArea();

    public OptimizeToolWindowPanel(Project project) {
        this.project = project;
        setLayout(new BorderLayout());

        tabs.add("Упрощение", createOptimizationTab(OptimizationType.SIMPLIFY));
        tabs.add("Производительность", createOptimizationTab(OptimizationType.PERFORMANCE));
        tabs.add("Рекомендации", createOptimizationTab(OptimizationType.ANALYSIS));

        add(tabs, BorderLayout.CENTER);
    }

    private JPanel createOptimizationTab(OptimizationType type) {
        JPanel panel = new JPanel(new BorderLayout());
        JButton optimizeBtn = new JButton("Оптимизировать");
        JTextArea area = new JTextArea();
        area.setEditable(false);

        optimizeBtn.addActionListener(e -> {
            String code = originalArea.getText();
            String result = CodeOptimizer.optimize(code, type);
            area.setText(result);
        });

        panel.add(optimizeBtn, BorderLayout.NORTH);
        panel.add(new JScrollPane(area), BorderLayout.CENTER);
        return panel;
    }

    public static void show(Project project, String selectedCode) {
        ToolWindow toolWindow = ToolWindowManager.getInstance(project).getToolWindow(OptimizeToolWindowFactory.ID);
        if (toolWindow == null) return;

        toolWindow.activate(() -> {
            if (instance != null) {
                instance.originalArea.setText(selectedCode);
            }
        });
    }
}
