package ru.qa.megagenerator.aiAssistant.enums;

import com.fasterxml.jackson.annotation.JsonCreator;

public enum FileType {
    DIRECTORY,
    FILE;

    @JsonCreator
    public static FileType fromString(String value) {
        return FileType.valueOf(value.toUpperCase());
    }

    @Override
    public String toString() {
        return name().toLowerCase();
    }
}
