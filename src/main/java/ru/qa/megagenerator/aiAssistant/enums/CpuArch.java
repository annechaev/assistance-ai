package ru.qa.megagenerator.aiAssistant.enums;

import java.util.Locale;

public enum CpuArch {

    AMD64,
    ARM64;

    public static CpuArch get() {
        String arch = System.getProperty("os.arch").toLowerCase(Locale.ROOT);
        return arch.contains("arm") || arch.contains("aarch64") ? CpuArch.ARM64 : CpuArch.AMD64;
    }
}
