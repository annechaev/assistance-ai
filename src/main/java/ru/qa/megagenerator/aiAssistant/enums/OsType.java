package ru.qa.megagenerator.aiAssistant.enums;

import java.util.Locale;

public enum OsType {

    WINDOWS,
    LINUX,
    MAC;

    public static OsType get() {
        String os = System.getProperty("os.name").toLowerCase(Locale.ROOT);

        if (os.contains("win")) return OsType.WINDOWS;
        if (os.contains("mac")) return OsType.MAC;
        return OsType.LINUX;
    }

}
