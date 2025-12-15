package ru.qa.megagenerator.aiAssistant.settings.configurable;

import org.jetbrains.annotations.NotNull;
import ru.qa.megagenerator.aiAssistant.annotations.ConfigurablePage;
import ru.qa.megagenerator.aiAssistant.interfaces.ConfigurableFormInterface;
import ru.qa.megagenerator.aiAssistant.settings.forms.ModelForm;

@ConfigurablePage
public class ModelConfigurable extends AbstractConfigurable {

    public static final String NAME = "Model";
    ModelForm form = new ModelForm();

    @Override
    public @NotNull String getDisplayName() {
        return NAME;
    }

    @Override
    public ConfigurableFormInterface getForm() {
        return form;
    }
}
