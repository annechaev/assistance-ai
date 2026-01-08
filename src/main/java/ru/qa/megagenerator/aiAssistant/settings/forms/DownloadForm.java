package ru.qa.megagenerator.aiAssistant.settings.forms;

import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.progress.ProgressIndicator;
import ru.qa.megagenerator.aiAssistant.clients.dto.ollama.CacheOllamaRelease;
import ru.qa.megagenerator.aiAssistant.clients.dto.ollama.OllamaRelease;
import ru.qa.megagenerator.aiAssistant.interfaces.ConfigurableFormInterface;
import ru.qa.megagenerator.aiAssistant.services.ollama.OllamaReleaseService;

import javax.swing.*;

import static ru.qa.megagenerator.aiAssistant.services.ollama.OllamaReleaseService.getFromDisk;
import static ru.qa.megagenerator.aiAssistant.services.ollama.OllamaService.createStructure;

public class DownloadForm implements ConfigurableFormInterface {
    private JPanel root;
    private JPanel content;
    private JPanel modelPanel;
    private JButton downloadButton;
    private JProgressBar downloadProgressBar;
    private JPanel ollamaVersionPanel;
    private JLabel versionLabel;

    public DownloadForm() {
        createStructure();
        translate();
        OllamaReleaseService service = ApplicationManager.getApplication().getService(OllamaReleaseService.class);
        setFields();

//        updateOllamaVersionButton.addActionListener(e -> {
//            ollamaStatusLabel.setVisible(false);
//            service.setListener(new OllamaReleaseListener() {
//                @Override
//                public void onReleasesUpdated(CacheOllamaRelease cache) {
//                    if(!cache.getReleases().isEmpty()) {
//                        downloadButton.setEnabled(true);
//                        ollamaVersionBox.removeAllItems();
//                        cache.getReleases().forEach(ollamaVersionBox::addItem);
//                        ollamaVersionBox.setSelectedItem(cache.getReleases().get(0));
//                        accessibleVersionButtons(cache.getReleases().get(0));
//                    }
//                }
//
//                @Override
//                public void onReleaseUpdateFailed(Throwable error) {
//                    ollamaStatusLabel.setText(error.getMessage());
//                    ollamaStatusLabel.setVisible(true);
//                    downloadButton.setEnabled(true);
//                    cancelButton.setEnabled(false);
//                }
//            });
//            service.refreshAsync();
//        });
//        ollamaVersionBox.addItemListener(e -> {
//            if (e.getStateChange() == ItemEvent.SELECTED) {
//                Object selected = e.getItem();
//                accessibleVersionButtons((OllamaRelease) selected);
//            }
//        });
//        downloadButton.addActionListener(e -> {
//            System.out.println("Download");
//            downloadButton.setEnabled(false);
//            cancelButton.setEnabled(true);
//            ollamaVersionBox.setEnabled(false);
//            downloadProgressBar.setIndeterminate(true);
//            service.download((OllamaRelease) ollamaVersionBox.getSelectedItem(), downloadProgressBar, downloadButton);
//        });
//        cancelButton.addActionListener(e -> {
//            System.out.println("Cancel");
//            ProgressIndicator indicator = service.getCurrentIndicator();
//            if (indicator != null) {
//                indicator.cancel();
//            }
//            OllamaReleaseService.deleteVersion(ollamaVersionBox.getSelectedItem().toString());
//            downloadButton.setEnabled(true);
//            cancelButton.setEnabled(false);
//            downloadProgressBar.setIndeterminate(false);
//            accessibleVersionButtons((OllamaRelease) ollamaVersionBox.getSelectedItem());
//        });
    }

    private void setFields() {
        CacheOllamaRelease cache = getFromDisk();
        OllamaReleaseService service = ApplicationManager.getApplication().getService(OllamaReleaseService.class);
        System.out.println("Смотрим кэш: \n" + cache);
//        if(cache != null && cache.getReleases() != null && !cache.getReleases().isEmpty()) {
//            cache.getReleases().forEach(ollamaVersionBox::addItem);
//            OllamaRelease selectedRelease = cache.getReleases().get(0);
//            if(service.getUiIndicator() != null) {
//                OllamaRelease ollamaRelease = cache.getReleases().stream()
//                        .filter(e -> e.getTagName().equals(service.getUiIndicator().getRelease()
//                                .getTagName()))
//                        .findAny().orElse(null);
//                if(ollamaRelease != null) {
//                    List<Path> pathList = FileUtils.getVersionFiles(ollamaRelease.getTagName());
//                    pathList.forEach(path -> System.out.println(path.getFileName()));
//                    boolean isDownloading = ollamaRelease.isDownloading();
//                    if(isDownloading) {
//                        System.out.println("Статус - скачивание");
//                        selectedRelease = ollamaRelease;
//                        service.getUiIndicator().rebind(downloadProgressBar, downloadButton);
//                    }
//                }
//            }
//
//            ollamaVersionBox.setSelectedItem(selectedRelease);
//            accessibleVersionButtons(selectedRelease);
//        } else {
//            String update = "Click update";
//            ollamaVersionBox.addItem(update);
//            ollamaVersionBox.setSelectedItem(update);
//            downloadButton.setEnabled(false);
//            cancelButton.setEnabled(false);
//        }
    }

    public void translate() {
//        setPanelBorderWithTitle(ollamaPanel, "Ollama");
    }

    @Override
    public void save() {

    }

    @Override
    public boolean isModified() {
        return false;
    }

    @Override
    public JComponent rootPane() {
        return root;
    }

    private void accessibleVersionButtons(OllamaRelease releaseVersion) {
        boolean isDownloadable = releaseVersion.isDownloadable();
        boolean isDownloading = releaseVersion.isDownloading();
        boolean isDownloaded = releaseVersion.isDownloaded();
        if(isDownloaded)
            downloadProgressBar.setValue(100);
        else if(isDownloadable)
            downloadProgressBar.setValue(0);
        downloadButton.setEnabled(isDownloadable);
        downloadButton.setVisible(!isDownloaded);
//        cancelButton.setEnabled(!isDownloadable);
//        ollamaVersionBox.setEnabled(!isDownloading);
//        updateOllamaVersionButton.setEnabled(!isDownloading);
    }
}
