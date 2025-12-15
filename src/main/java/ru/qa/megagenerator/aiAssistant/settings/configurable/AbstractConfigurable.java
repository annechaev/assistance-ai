package ru.qa.megagenerator.aiAssistant.settings.configurable;

import com.intellij.openapi.options.SearchableConfigurable;
import org.jetbrains.annotations.NotNull;
import ru.qa.megagenerator.aiAssistant.interfaces.ConfigurableFormInterface;


public abstract class AbstractConfigurable implements SearchableConfigurable {

    @Override
    public @NotNull String getId() {
        System.out.println(getClass().getCanonicalName());
        return getClass().getCanonicalName();
    }

    @Override
    public abstract @NotNull String getDisplayName();

    @Override
    public javax.swing.@NotNull JComponent createComponent() {
        return getForm().rootPane();
    }

    public abstract ConfigurableFormInterface getForm();

    @Override
    public boolean isModified() {
        return getForm().isModified();
    }

    @Override
    public void apply() {
        getForm().save();
    }
}
