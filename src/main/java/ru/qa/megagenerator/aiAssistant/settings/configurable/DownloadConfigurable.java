package ru.qa.megagenerator.aiAssistant.settings.configurable;

import org.jetbrains.annotations.NotNull;
import ru.qa.megagenerator.aiAssistant.annotations.ConfigurablePage;
import ru.qa.megagenerator.aiAssistant.interfaces.ConfigurableFormInterface;
import ru.qa.megagenerator.aiAssistant.settings.forms.DownloadForm;
import ru.qa.megagenerator.aiAssistant.settings.forms.ModelForm;

@ConfigurablePage
public class DownloadConfigurable extends AbstractConfigurable {

    public static final String NAME = "Download";
    DownloadForm form = new DownloadForm();

    @Override
    public @NotNull String getDisplayName() {
        return NAME;
    }

    @Override
    public ConfigurableFormInterface getForm() {
        return form;
    }
}
