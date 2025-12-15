package ru.qa.megagenerator.aiAssistant.aiModels;

import com.jetbrains.Service;
import ru.qa.megagenerator.aiAssistant.enums.AiModelType;
import ru.qa.megagenerator.aiAssistant.interfaces.AiModelImpl;

@Service
public class DeepSeekModel implements AiModelImpl {

    public static final String ID = "DEEP_SEEK";

    @Override
    public String id() {
        return ID;
    }

    @Override
    public boolean isLocal() {
        return true;
    }

    @Override
    public boolean isRemote() {
        return true;
    }

    @Override
    public String formName() {
        return "deepseek";
    }

    @Override
    public AiModelType type() {
        return AiModelType.LLM;
    }
}
