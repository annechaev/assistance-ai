package ru.qa.megagenerator.aiAssistant.services.ollama;


import ru.qa.megagenerator.aiAssistant.utils.common.FileNode;
import ru.qa.megagenerator.aiAssistant.utils.common.FileUtils;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

import static ru.qa.megagenerator.aiAssistant.utils.common.FileUtils.*;

public final class OllamaService {

    public static final String RELEASE_CACHE_FILE_NAME = "ollama-releases.json";
    public static final String MODELS_CACHE_FILE_NAME = "ollama-models.json";
    public static final List<String> EXTENSION_LIST = List.of(".tgz", ".zip");
    public static final String VERSION_PATTERN = "^[vV]?\\d+\\.\\d+\\.\\d+$";

    private OllamaService() {
    }

    public static Path ollamaHome() {
        return FileUtils.createFolder(FileUtils.getUserHome(), "ollama");
    }

    public static Path ollamaData() {
        return FileUtils.createFolder(ollamaHome(), "ollama-data");
    }

    public static Path ollamaBin() {
        return FileUtils.createFolder(ollamaHome(), "bin");
    }

    public static Path ollamaCache() {
        Path path =  FileUtils.createFolder(FileUtils.getUserHome(), "cache");
        createFile(path.resolve(RELEASE_CACHE_FILE_NAME), "{}");
        createFile(path.resolve(MODELS_CACHE_FILE_NAME), "{}");
        return path;
    }

    public static void createStructure() {
        FileNode fileNode = getFromResources("ollama-structure.json", FileNode.class);
        if(fileNode != null) {
            try {
                FileUtils.createStructure(getUserHome(), fileNode);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
