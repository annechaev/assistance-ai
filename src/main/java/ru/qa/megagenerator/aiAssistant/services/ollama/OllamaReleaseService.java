package ru.qa.megagenerator.aiAssistant.services.ollama;

import com.fasterxml.jackson.core.type.TypeReference;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.application.ModalityState;
import com.intellij.openapi.components.Service;
import com.intellij.openapi.progress.ProgressIndicator;
import com.intellij.openapi.progress.Task;
import com.intellij.openapi.util.io.NioFiles;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import ru.qa.megagenerator.aiAssistant.clients.dto.ollama.CacheOllamaRelease;
import ru.qa.megagenerator.aiAssistant.clients.dto.ollama.OllamaRelease;
import ru.qa.megagenerator.aiAssistant.enums.BalloonType;
import ru.qa.megagenerator.aiAssistant.enums.CpuArch;
import ru.qa.megagenerator.aiAssistant.enums.DownloadFileState;
import ru.qa.megagenerator.aiAssistant.enums.OsType;
import ru.qa.megagenerator.aiAssistant.inners.OllamaReleaseItem;
import ru.qa.megagenerator.aiAssistant.interfaces.OllamaReleaseListener;
import ru.qa.megagenerator.aiAssistant.services.MockService;
import ru.qa.megagenerator.aiAssistant.utils.common.FileUtils;
import ru.qa.megagenerator.aiAssistant.utils.form.BalloonUtils;
import ru.qa.megagenerator.aiAssistant.utils.form.UiMirroringProgressIndicator;

import javax.swing.*;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.function.Supplier;

import static com.google.common.net.HttpHeaders.AUTHORIZATION;
import static ru.qa.megagenerator.aiAssistant.constants.CommonConstants.*;
import static ru.qa.megagenerator.aiAssistant.enums.Endpoint.*;
import static ru.qa.megagenerator.aiAssistant.services.ollama.OllamaService.*;
import static ru.qa.megagenerator.aiAssistant.services.ollama.OllamaState.DEFAULT_CURRENT_RELEASE;

@Service
public final class OllamaReleaseService {

    private OllamaReleaseListener listener;
    private ProgressIndicator currentIndicator;
    private UiMirroringProgressIndicator uiIndicator;

    private final Duration DURATION = Duration.of(24, ChronoUnit.HOURS);
    private final String OLLAMA_FILE_DEFAULT_NAME = "ollama.tgz";

    public UiMirroringProgressIndicator getUiIndicator() {
        return uiIndicator;
    }

    public void setListener(OllamaReleaseListener listener) {
        this.listener = listener;
    }

    public ProgressIndicator getCurrentIndicator() {
        return currentIndicator;
    }

    public void asyncLatestReleaseIfNeed() {
        ModalityState currentModality = ModalityState.current();
        OllamaState state = ApplicationManager.getApplication().getService(OllamaState.class);
        OllamaReleaseItem item = new OllamaReleaseItem();
        setLastReleaseFromState(item);
        System.out.println("Является ли стартом? " + state.getCurrentRelease().equals(DEFAULT_CURRENT_RELEASE));
        System.out.println("Прошли ли 24 часа? " + (System.currentTimeMillis() - state.getLastUpdate() >= DURATION.toMillis()));
        if(state.getCurrentRelease().equals(DEFAULT_CURRENT_RELEASE)
                || System.currentTimeMillis() - state.getLastUpdate() >= DURATION.toMillis()) {
            ApplicationManager.getApplication().executeOnPooledThread(() -> {
                updateLastReleaseFromGit(item);
                supplierListener(() -> item, currentModality);
            });
        }
        supplierListener(() -> item, currentModality);
    }

    private void setLastReleaseFromState(OllamaReleaseItem item) {
        OllamaState state = ApplicationManager.getApplication().getService(OllamaState.class);
        DownloadFileState downloadFileState = resolveState(ollamaBin());
        item.setCurrent(state.getCurrentRelease());
        item.setNewVersion(state.getLastRelease());
        item.setState(downloadFileState);
    }

    private void supplierListener(Supplier<OllamaReleaseItem> supplier, ModalityState currentModality) {
        OllamaReleaseItem item = supplier.get();
        try {
            if (listener != null) {
                ApplicationManager.getApplication().invokeLater(() -> {
                    // В IDEA 2025 это обязательная обертка для модификации UI-моделей в EDT
                    com.intellij.openapi.application.WriteIntentReadAction.run((Runnable) () -> {
                        listener.onReleasesUpdateSuccess(item);
                    });
                }, currentModality); // Обязательно current() для окна настроек
            }
        } catch (Exception e) {
            if (listener != null) {
                ApplicationManager.getApplication().invokeLater(() -> {
                    listener.onReleaseUpdateFailed(e);
                }, currentModality);
            }
        }
    }

    private void updateLastReleaseFromGit(OllamaReleaseItem item) {
        OllamaState state = ApplicationManager.getApplication().getService(OllamaState.class);
        OllamaRelease latest = getLatest();
        state.setLastRelease(latest.getTagName());
        state.setLastReleaseSize(latest.calculateSize());
        state.setLastUpdate(System.currentTimeMillis());
        String currentRelease = state.getCurrentRelease().equals(DEFAULT_CURRENT_RELEASE) ? state.getLastRelease()
                : state.getCurrentRelease();
        state.setCurrentRelease(currentRelease);
        item.setCurrent(currentRelease);
        item.setState(item.getState() == DownloadFileState.INSTALLED
                && !latest.getTagName().equals(state.getCurrentRelease())
                ? DownloadFileState.UPDATABLE : item.getState());
        item.setNewVersion(latest.getTagName());
        item.setRelease(latest);
    }

    public static String downloadUrl(String version) {
        String file = getFileName();
        return OLLAMA_DOWNLOAD_RELEASE.getUrl().formatted(version, file);
    }

    public static String getFileName() {
        OsType os = OsType.LINUX;

        return switch (os) {
            case LINUX  -> "ollama-linux-" + getCpuArchString() + ".tgz";
            case MAC -> "ollama-darwin-" + getCpuArchString() + ".tgz";
            case WINDOWS  -> "ollama-windows-amd64.zip";
        };
    }

    public static String getCpuArchString() {
        CpuArch arch = CpuArch.get();
        return arch == CpuArch.ARM64 ? "arm64" : "amd64";
    }

    public static List<OllamaRelease> getReleases() {
        ollamaCache();
        ollamaHome();
        HttpRequest httpRequest = HttpRequest.newBuilder()
                .uri(URI.create(OLLAMA_RELEASE.getUrl()))
                .header(AUTHORIZATION, "Bearer " + TOKEN)
                .GET().build();
        try {
            HttpResponse<String> response = HTTP_CLIENT.send(httpRequest, HttpResponse.BodyHandlers.ofString());
            List<OllamaRelease> list = MAPPER.readValue(response.body(), new TypeReference<List<OllamaRelease>>() {});
            System.out.println(list);
            return list;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static OllamaRelease getLatest() {
//        return ApplicationManager.getApplication().getService(MockService.class)
//                .getNewRelease();
        HttpRequest httpRequest = HttpRequest.newBuilder()
                .uri(URI.create(OLLAMA_LATEST_RELEASE.getUrl()))
                .header(AUTHORIZATION, "Bearer " + TOKEN)
                .GET().build();
        try {
            HttpResponse<String> response = HTTP_CLIENT.send(httpRequest, HttpResponse.BodyHandlers.ofString());
            return MAPPER.readValue(response.body(), OllamaRelease.class);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void downloadRelease(OllamaRelease release, JProgressBar progressBar, String pattern) {
        Task.Backgroundable task = new Task.Backgroundable(null, "Downloading ollama", true) {
            @Override
            public void run(@NotNull ProgressIndicator indicator) {
                System.out.println("Поехали качать");
                release.setState(DownloadFileState.DOWNLOADING);
                currentIndicator = indicator;
                uiIndicator = new UiMirroringProgressIndicator(indicator, progressBar, release);
                progressBar.setIndeterminate(true);
                indicator.setIndeterminate(false);
                indicator.setText("Download ollama %s".formatted(release.getTagName()));
                Path target = ollamaBin().resolve(OLLAMA_FILE_DEFAULT_NAME);
                Path temp = ollamaBin().resolve(getFileName() + ".part");
                try {
                    FileUtils.downloadFile(downloadUrl(release.getTagName()), temp, uiIndicator);
                    Files.move(
                            temp,
                            target,
                            StandardCopyOption.REPLACE_EXISTING,
                            StandardCopyOption.ATOMIC_MOVE
                    );
                    OllamaState state = ApplicationManager.getApplication().getService(OllamaState.class);
                    state.setCurrentRelease(release.getTagName());
                    state.setLastReleaseSize(release.calculateSize());
                } catch (Exception e) {
                    try {
                        System.out.println("Что-то пошло не так!");
                        e.printStackTrace();
                        Files.deleteIfExists(temp);
                    } catch (IOException ex) {
                        throw new RuntimeException(ex);
                    }
                }
            }

            @Override
            public void onSuccess() {
                System.out.println("Успешно!");
                uiIndicator.refresh();
                release.setState(DownloadFileState.INSTALLED);
                OllamaState state = ApplicationManager.getApplication().getService(OllamaState.class);
                state.setCurrentRelease(release.getTagName());
                BalloonUtils.show(pattern.formatted(release) + " completed", BalloonType.SUCCESS);
            }

            @Override
            public void onCancel() {
                System.out.println("Зафакапились!");
                uiIndicator.refresh();
                release.setState(DownloadFileState.FAILED);
                BalloonUtils.show(pattern.formatted(release) + " failed", BalloonType.ERROR);
                progressBar.setValue(0);
                progressBar.setIndeterminate(false);
            }
        };
        task.queue();
    }

    public void moveRelease(String version, Path filePath) {
        OllamaState state = ApplicationManager.getApplication().getService(OllamaState.class);
        String title = "Copy ollama";
        Task.Backgroundable task = new Task.Backgroundable(null, title, true) {

            @Override
            public void run(@NotNull ProgressIndicator progressIndicator) {
                Path target = ollamaBin().resolve(OLLAMA_FILE_DEFAULT_NAME);
                progressIndicator.setIndeterminate(false);
                try {
                    FileUtils.copyFile(filePath, target, progressIndicator);
                    Files.copy(filePath, target, StandardCopyOption.REPLACE_EXISTING);
                    state.setCurrentRelease(version);
                } catch (IOException e) {
                    try {
                        Files.deleteIfExists(target);
                    } catch (IOException ignored) {
                        ignored.printStackTrace();
                    }
                }
            }

            @Override
            public void onSuccess() {
                BalloonUtils.show(title + " completed", BalloonType.SUCCESS);
            }

            @Override
            public void onCancel() {
                BalloonUtils.show(title + " failed", BalloonType.ERROR);
            }

        };
        task.queue();
    }

    public static List<String> getDownloadedVersions() {
        try {
            return Files.list(ollamaHome())
                    .filter(Files::isDirectory)
                    .map(p -> p.getFileName().toString())
                    .toList();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void deleteVersion(String version) {
        try {
            NioFiles.deleteRecursively(ollamaHome().resolve(version));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void delete() {
        FileUtils.removeAllFiles(ollamaBin());
    }

    public static CacheOllamaRelease getFromDisk() {
        ollamaCache();
        ollamaHome();
        return FileUtils.getContentFromFile(ollamaCache().resolve(RELEASE_CACHE_FILE_NAME), CacheOllamaRelease.class);
    }

    public static DownloadFileState resolveState(Path binDir) {
        if (!Files.isDirectory(binDir)) {
            return DownloadFileState.NOT_INSTALLED;
        }
        List<Path> files = FileUtils.getDirectoryFiles(binDir);
        boolean downloading = files.stream()
                .map(p -> p.getFileName().toString())
                .anyMatch(name -> name.endsWith(".part"));

        if (downloading) return DownloadFileState.DOWNLOADING;

        boolean installed = files.stream()
                .map(p -> p.getFileName().toString())
                .anyMatch(EXTENSION_LIST::contains);

        return installed ? DownloadFileState.INSTALLED : DownloadFileState.NOT_INSTALLED;
    }

    public static String resolveStatusMessage(
            DownloadFileState state,
            String serviceName,
            @Nullable String currentVersion,
            @Nullable String newVersion,
            @Nullable Float sizeGb
    ) {
        return state.message()
                .map(pattern -> switch (state) {
                    case NOT_INSTALLED -> pattern.formatted(serviceName, sizeGb);
                    case DOWNLOADING, INSTALLED -> pattern.formatted(serviceName);
                    case UPDATABLE -> pattern.formatted(currentVersion, newVersion, sizeGb);
                    default -> null;
                }).orElse(null);
    }
}
