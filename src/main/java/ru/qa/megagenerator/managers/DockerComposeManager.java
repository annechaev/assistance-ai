package ru.qa.megagenerator.managers;

import com.intellij.notification.Notification;
import com.intellij.notification.NotificationType;
import com.intellij.notification.Notifications;
import com.intellij.openapi.application.ApplicationManager;

import java.io.*;
import java.nio.file.*;
import java.util.concurrent.TimeUnit;

public class DockerComposeManager {

    // путь внутри jar-ресурсов
    private static final String RESOURCE_PATH = "/docker/docker-compose-ollama.yml";

    // куда временно копируем файл для запуска
    private static final Path TEMP_COMPOSE_FILE =
            Paths.get(System.getProperty("java.io.tmpdir"), "docker-compose-ollama.yml");

    private static final String HEALTHCHECK_URL = "http://localhost:11434/api/tags";

    public static void runLocal() {
        ApplicationManager.getApplication().executeOnPooledThread(() -> {
            if (DockerComposeManager.ensureDeepSeekEnvironment()) {
                Notifications.Bus.notify(
                        new Notification("AI", "DeepSeek", "Окружение запущено и готово.",
                                NotificationType.INFORMATION)
                );
            } else {
                Notifications.Bus.notify(
                        new Notification("AI", "DeepSeek", "Не удалось запустить Docker окружение.",
                                NotificationType.ERROR)
                );
            }
        });
    }

    /**
     * Проверяет, установлен ли Docker.
     */
    public static boolean isDockerInstalled() {
        try {
            Process process = new ProcessBuilder("docker", "--version")
                    .redirectErrorStream(true)
                    .start();
            process.waitFor(3, TimeUnit.SECONDS);
            return process.exitValue() == 0;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Проверяет, запущен ли контейнер Ollama.
     */
    public static boolean isOllamaRunning() {
        try {
            Process process = new ProcessBuilder("docker", "ps", "--filter", "name=ollama", "--format", "{{.Names}}")
                    .redirectErrorStream(true)
                    .start();

            String output = new String(process.getInputStream().readAllBytes()).trim();
            return output.contains("ollama");
        } catch (IOException e) {
            return false;
        }
    }

    /**
     * Копирует docker-compose.yml из ресурсов во временную директорию.
     */
    private static Path extractComposeFile() throws IOException {
        if (Files.exists(TEMP_COMPOSE_FILE)) {
            return TEMP_COMPOSE_FILE;
        }

        try (InputStream in = DockerComposeManager.class.getResourceAsStream(RESOURCE_PATH)) {
            if (in == null) {
                throw new FileNotFoundException("Не найден ресурс: " + RESOURCE_PATH);
            }
            Files.copy(in, TEMP_COMPOSE_FILE, StandardCopyOption.REPLACE_EXISTING);
            System.out.println("📦 docker-compose.yml скопирован во временный каталог: " + TEMP_COMPOSE_FILE);
        }
        return TEMP_COMPOSE_FILE;
    }

    /**
     * Запускает docker-compose (создаёт контейнер Ollama + DeepSeek).
     */
    public static void startDockerCompose() throws IOException, InterruptedException {
        Path composePath = extractComposeFile();

        System.out.println("🚀 Запуск DeepSeek окружения через Docker Compose...");
        ProcessBuilder pb = new ProcessBuilder("docker", "compose", "-f", composePath.toString(), "up", "-d");
        pb.inheritIO();
        Process process = pb.start();
        process.waitFor();
    }

    /**
     * Ждёт, пока Ollama API будет доступен (до 60 секунд).
     */
    public static boolean waitForOllamaReady() {
        System.out.println("⏳ Ожидание готовности Ollama API...");
        long start = System.currentTimeMillis();

        while (System.currentTimeMillis() - start < 60_000) {
            try {
                Process curl = new ProcessBuilder("curl", "-s", HEALTHCHECK_URL)
                        .redirectErrorStream(true)
                        .start();
                String response = new String(curl.getInputStream().readAllBytes());
                if (response.contains("deepseek") || response.contains("model")) {
                    System.out.println("✅ Ollama API готов к работе.");
                    return true;
                }
                Thread.sleep(3000);
            } catch (Exception ignored) {}
        }
        System.err.println("❌ Не удалось дождаться готовности Ollama API.");
        return false;
    }

    /**
     * Основной метод: проверяет Docker → поднимает окружение → ждёт готовности.
     */
    public static boolean ensureDeepSeekEnvironment() {
        try {
            if (!isDockerInstalled()) {
                System.err.println("❌ Docker не установлен. Установите Docker Desktop.");
                return false;
            }

            if (!isOllamaRunning()) {
                startDockerCompose();
            } else {
                System.out.println("ℹ️ Ollama уже запущен, пропускаем запуск.");
            }

            return waitForOllamaReady();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}