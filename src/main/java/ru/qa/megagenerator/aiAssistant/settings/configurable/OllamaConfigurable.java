package ru.qa.megagenerator.aiAssistant.settings.configurable;

import org.jetbrains.annotations.NotNull;
import ru.qa.megagenerator.aiAssistant.annotations.ConfigurablePage;
import ru.qa.megagenerator.aiAssistant.interfaces.ConfigurableFormInterface;
import ru.qa.megagenerator.aiAssistant.settings.forms.OllamaDownloadPanel;
import ru.qa.megagenerator.aiAssistant.settings.forms.OllamaForm;

@ConfigurablePage
public class OllamaConfigurable extends AbstractConfigurable {

    public static final String NAME = "Ollama";
    OllamaForm form = new OllamaForm();

    @Override
    public @NotNull String getDisplayName() {
        return NAME;
    }

    @Override
    public ConfigurableFormInterface getForm() {
        return form;
    }
}
