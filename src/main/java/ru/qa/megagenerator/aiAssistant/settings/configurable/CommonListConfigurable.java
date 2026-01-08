package ru.qa.megagenerator.aiAssistant.settings.configurable;

import com.intellij.openapi.options.Configurable;
import com.intellij.openapi.options.SearchableConfigurable;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.reflections.Reflections;
import ru.qa.megagenerator.aiAssistant.annotations.ConfigurablePage;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;

import static com.intellij.ide.actionsOnSave.ActionsOnSaveConfigurable.createGoToPageInSettingsLink;
import static ru.qa.megagenerator.aiAssistant.utils.common.ReflectUtils.getAnnotatedInstances;

public class CommonListConfigurable implements SearchableConfigurable, Configurable.Composite {
    private JPanel panel;
    public static final String NAME = "MG Ai assistant";
    private List<Configurable> pageList = new ArrayList<>();

    @Nls(capitalization = Nls.Capitalization.Title)
    @Override
    public String getDisplayName() {
        return NAME;
    }

    @Override
    public Configurable @NotNull [] getConfigurables() {
        return getPageList().toArray(Configurable[]::new);
    }

    @Nullable
    @Override
    public JComponent createComponent() {
        if(panel == null) {
            panel = new JPanel();
            panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
            getPageList().forEach(config -> {
                panel.add(createGoToPageInSettingsLink(config.getDisplayName(), config.getClass().getCanonicalName()));
            });
        }
        return panel;
    }

    @Override
    public boolean isModified() {
        return false;
    }

    @Override
    public void apply() {

    }

    @NotNull
    @Override
    public String getId() {
        return CommonListConfigurable.class.getCanonicalName();
    }

    private List<Configurable> initializePageList() {
        Reflections reflections = new Reflections(CommonListConfigurable.class.getPackageName());
        pageList = getAnnotatedInstances(ConfigurablePage.class, Configurable.class, reflections);
        return pageList;
    }

    public List<Configurable> getPageList() {
        pageList = pageList.isEmpty() ? initializePageList() : pageList;
        return pageList;
    }
}
