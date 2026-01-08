package ru.qa.megagenerator.aiAssistant.utils.common;

import ru.qa.megagenerator.aiAssistant.enums.FileType;

import java.util.List;

public class FileNode {
    private String name;
    private FileType type; // "directory" или "file"
    private String resourcePath; // только для файлов
    private String content;
    private List<FileNode> children;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public FileType getType() {
        return type;
    }

    public void setType(FileType type) {
        this.type = type;
    }

    public String getResourcePath() {
        return resourcePath;
    }

    public void setResourcePath(String resourcePath) {
        this.resourcePath = resourcePath;
    }

    public List<FileNode> getChildren() {
        return children;
    }

    public void setChildren(List<FileNode> children) {
        this.children = children;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
