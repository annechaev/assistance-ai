package ru.qa.megagenerator.aiAssistant.utils.common;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

public final class FileUtils {

    public static String getContentFromFile(String resourcePath) {
        try (InputStream stream = FileUtils.class.getClassLoader().getResourceAsStream(resourcePath)) {
            if (stream != null) {
                return new String(stream.readAllBytes(), StandardCharsets.UTF_8);
            }
            throw new RuntimeException("В ресурсах файл не найден: " + resourcePath);
        } catch (IOException e) {
            throw new RuntimeException("Ошибка при чтении из файла в ресурсах: " + resourcePath, e);
        }
    }

}
