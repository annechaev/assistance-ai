package ru.qa.megagenerator.aiAssistant.utils.common;

import com.intellij.openapi.progress.ProgressIndicator;
import com.intellij.openapi.util.Condition;
import com.intellij.openapi.vfs.VirtualFile;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import ru.qa.megagenerator.aiAssistant.services.ollama.OllamaService;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static ru.qa.megagenerator.aiAssistant.constants.CommonConstants.MAPPER;
import static ru.qa.megagenerator.aiAssistant.enums.FileType.DIRECTORY;
import static ru.qa.megagenerator.aiAssistant.enums.FileType.FILE;

public final class FileUtils {

    public static String getContentFromResources(String resourcePath) {
        try (InputStream stream = FileUtils.class.getClassLoader().getResourceAsStream(resourcePath)) {
            if (stream != null) {
                return new String(stream.readAllBytes(), StandardCharsets.UTF_8);
            }
            throw new RuntimeException("В ресурсах файл не найден: " + resourcePath);
        } catch (IOException e) {
            throw new RuntimeException("Ошибка при чтении из файла в ресурсах: " + resourcePath, e);
        }
    }

    public static <T> T getContentFromFile(Path path, Class<T> clazz) {
        try {
            if(Files.exists(path)) {
                return MAPPER.readValue(path.toFile(), clazz);
            } else {
                return null;
            }
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static <T> T getContentFromString(String content, Class<T> clazz) {
        try {
            return MAPPER.readValue(content, clazz);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static <T> T getFromResources(String path, Class<T> clazz) {
        return getContentFromString(getContentFromResources(path), clazz);
    }

    public static <T> void saveJsonToFile(Path path, T content) {
        try {
            Files.createDirectories(path.getParent());
            MAPPER.writerWithDefaultPrettyPrinter()
                    .writeValue(path.toFile(), content);
        } catch (IOException e) {
            throw new RuntimeException("Failed to save json to file: " + path, e);
        }
    }

    public static Path getUserHome() {
        Path baseDir = Paths.get(System.getProperty("user.home"), ".mg-ai-assistant");
        try {
            Files.createDirectories(baseDir);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return baseDir;
    }

    public static List<Path> getDirectoryFiles(Path path) {
        try(Stream<Path> stream = Files.list(path)) {
            return  stream.collect(Collectors.toList());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static List<Path> getVersionFiles(String version) {
        Path path = OllamaService.ollamaHome().resolve(version);
        return Files.exists(path) ? getDirectoryFiles(path) : Collections.emptyList();
    }

    public static Path createFolder(Path dir, String name) {
        Path newDir = dir.resolve(name);
        try {
            Files.createDirectories(newDir);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return newDir;
    }

    public static void createFile(Path file, String content) {
        if (Files.notExists(file)) {
            try {
                Files.createDirectories(file.getParent()); // безопасно, если папки уже есть
                Files.writeString(file, content, StandardOpenOption.CREATE_NEW);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public static void downloadFile(String url, Path target) throws IOException {
        downloadFile(url, target, null);
    }

//    public static void downloadFile(String url, Path target, ProgressIndicator indicator) throws IOException {
//        HttpURLConnection conn = (HttpURLConnection) new URL(url).openConnection();
//        conn.setRequestMethod("GET");
//
//        long total = conn.getContentLengthLong();
//
//        try (InputStream in = conn.getInputStream();
//             OutputStream out = Files.newOutputStream(target)) {
//            byte[] buffer = new byte[8192];
//            long read = 0;
//            int n;
//
//            while ((n = in.read(buffer)) > 0) {
//                out.write(buffer, 0, n);
//                read += n;
//                if(indicator != null) {
//                    if (total > 0) {
//                        indicator.setFraction((double) read / total);
//                    }
//                    indicator.checkCanceled();
//                }
//            }
//        }
//    }

    public static void downloadFile(@NotNull String url, @NotNull Path target, @Nullable ProgressIndicator indicator
    ) throws IOException {

        HttpURLConnection conn = (HttpURLConnection) new URL(url).openConnection();
        conn.setRequestMethod("GET");

        long total = conn.getContentLengthLong();

        try (InputStream in = conn.getInputStream();
             OutputStream out = Files.newOutputStream(target)) {

            transfer(in, out, total, indicator);
        }
    }

    public static void transfer(@NotNull InputStream in, @NotNull OutputStream out, long totalBytes,
            @Nullable ProgressIndicator indicator) throws IOException {

        byte[] buffer = new byte[8192];
        long transferred = 0;
        int n;

        while ((n = in.read(buffer)) >= 0) {
            out.write(buffer, 0, n);
            transferred += n;

            if (indicator != null) {
                if (totalBytes > 0) {
                    indicator.setFraction((double) transferred / totalBytes);
                }
                indicator.checkCanceled();
            }
        }
    }

    public static void copyFile(@NotNull Path source, @NotNull Path target, @Nullable ProgressIndicator indicator
    ) throws IOException {

        long total = Files.size(source);

        try (InputStream in = Files.newInputStream(source);
             OutputStream out = Files.newOutputStream(target,
                     StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING)) {
            transfer(in, out, total, indicator);
        }
    }

    public static void moveFile(@NotNull Path source, @NotNull Path target, @Nullable ProgressIndicator indicator
    ) throws IOException {

        copyFile(source, target, indicator);
        Files.delete(source);
    }

    public static void createStructure(Path parent, FileNode node) throws IOException {
        Path current = parent.resolve(node.getName());
        if (DIRECTORY.equals(node.getType())) {
            Files.createDirectories(current);
            if (node.getChildren() != null) {
                for (FileNode child : node.getChildren()) {
                    createStructure(current, child);
                }
            }
        } else if (FILE.equals(node.getType())) {
            if(node.getResourcePath() != null) {
                // Берём файл из ресурсов плагина
                try (InputStream in = FileUtils.class.getResourceAsStream(node.getResourcePath())) {
                    if (in == null) throw new IOException("Resource not found: " + node.getResourcePath());
                    Files.copy(in, current, StandardCopyOption.REPLACE_EXISTING);
                }
            } else {
                try {
                    Files.writeString(current, node.getContent(), StandardCharsets.UTF_8, StandardOpenOption.CREATE,
                            StandardOpenOption.TRUNCATE_EXISTING);
                } catch (FileAlreadyExistsException ignored) {
                    // файл уже существует — это ожидаемое поведение
                }
            }
        }
    }

    public static void removeAllFiles(Path dirPath) {
        try(Stream<Path> files = Files.list(dirPath)) {
            files.forEach(filePath -> {
                try {
                    Files.delete(filePath);
                } catch (IOException ignored) {}
            });
        } catch (IOException ignored) {}
    }

    public static Condition<VirtualFile> extensionFilter(String... extensionList) {
        return file -> Arrays.stream(extensionList)
                .anyMatch(extension -> file.getExtension() != null && file.getExtension().endsWith(extension));
    }
}
