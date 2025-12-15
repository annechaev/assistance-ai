package ru.qa.megagenerator.aiAssistant.settings.states;

import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.components.PersistentStateComponent;
import com.intellij.openapi.components.Service;
import com.intellij.openapi.components.State;
import com.intellij.openapi.components.Storage;
import com.intellij.util.xmlb.XmlSerializerUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import ru.qa.megagenerator.aiAssistant.interfaces.AiModelImpl;

import static ru.qa.megagenerator.aiAssistant.constants.CommonConstants.*;

@State(name = "model settings", storages = @Storage("ai-assistant model settings.xml"))
@Service
public final class ModelState implements PersistentStateComponent<ModelState> {

    private boolean isRemote = true;
    private boolean isLocal = false;
    private String remoteModelId = getAiModels().get(0).id();
    private String localModelId = getLocalModels().get(0).id();
    private String remoteUrl = "";
    private String remoteToken = "";

    public static ModelState getInstance() {
        return ApplicationManager.getApplication().getService(ModelState.class);
    }

    public AiModelImpl getRemoteModel() {
        return getRemoteModels().stream().filter(ai -> ai.id().equals(remoteModelId))
                .findFirst().orElse(null);
    }

    public AiModelImpl getLocalModel() {
        return getLocalModels().stream().filter(ai -> ai.id().equals(localModelId))
                .findFirst().orElse(null);
    }

    public void setRemoteModel(AiModelImpl remoteModel) {
        remoteModelId = remoteModel.id();
    }

    public void setLocalModel(AiModelImpl localModel) {
        localModelId = localModel.id();
    }

    public boolean isRemote() {
        return isRemote;
    }

    public void setIsRemote(boolean remote) {
        isRemote = remote;
    }

    public boolean isLocal() {
        return isLocal;
    }

    public void setIsLocal(boolean local) {
        isLocal = local;
    }

    public String getRemoteUrl() {
        return remoteUrl;
    }

    public void setRemoteUrl(String remoteUrl) {
        this.remoteUrl = remoteUrl;
    }

    public String getRemoteToken() {
        return remoteToken;
    }

    public void setRemoteToken(String remoteToken) {
        this.remoteToken = remoteToken;
    }

    @Override
    public @Nullable ModelState getState() {
        return this;
    }

    @Override
    public void loadState(@NotNull ModelState modelState) {
        XmlSerializerUtil.copyBean((modelState), this);
    }
}
