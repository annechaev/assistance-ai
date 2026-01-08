package ru.qa.megagenerator.aiAssistant.constants;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.reflections.Reflections;
import ru.qa.megagenerator.aiAssistant.annotations.ConfigurablePage;
import ru.qa.megagenerator.aiAssistant.interfaces.AiClient;
import ru.qa.megagenerator.aiAssistant.interfaces.AiModelImpl;
import ru.qa.megagenerator.aiAssistant.utils.common.ReflectUtils;

import java.net.http.HttpClient;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.stream.Collectors;
import java.util.List;

public class CommonConstants {

    public static String pack = Arrays.stream(ConfigurablePage.class.getPackageName().split("\\."))
            .limit(3).collect(Collectors.joining("."));
    public static Reflections reflections = new Reflections(pack);
    public static final HttpClient HTTP_CLIENT = HttpClient.newHttpClient();
    public static final ObjectMapper MAPPER = JsonMapper.builder().addModule(new JavaTimeModule())
            .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false).build();
    public static final String TOKEN = "";

    private static List<AiModelImpl> aiModels;
    private static List<AiClient> aiClients;

    public static List<AiModelImpl> getAiModels() {
        if (aiModels == null) {
            aiModels = ReflectUtils.getSubClassesServices(AiModelImpl.class);
        }
        return aiModels;
    }

    public static List<AiClient> getAiClients() {
        if (aiClients == null) {
            aiClients = ReflectUtils.getSubClassesServices(AiClient.class);
        }
        return aiClients;
    }

    public static List<AiModelImpl> getRemoteModels() {
        return  getAiModels().stream().filter(AiModelImpl::isRemote).toList();
    }

    public static List<AiModelImpl> getLocalModels() {
        return  getAiModels().stream().filter(AiModelImpl::isLocal).toList();
    }

    public static AiClient getByModel(AiModelImpl model) {
        return getAiClients().stream().filter(ai -> ai.modelId().equals(model.id())).findFirst()
                .orElse(null);
    }
}
