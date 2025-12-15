package ru.qa.megagenerator.aiAssistant.aiModels;

import com.jetbrains.Service;
import ru.qa.megagenerator.aiAssistant.enums.AiModelType;
import ru.qa.megagenerator.aiAssistant.interfaces.AiModelImpl;

@Service
public class ChatGPTModel implements AiModelImpl {

    public static final String ID = "CHAT_GPT";

    @Override
    public String id() {
        return ID;
    }

    @Override
    public boolean isLocal() {
        return false;
    }

    @Override
    public boolean isRemote() {
        return true;
    }

    @Override
    public String formName() {
        return "chatgpt";
    }

    @Override
    public AiModelType type() {
        return AiModelType.LLM;
    }
}
