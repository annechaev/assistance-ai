package ru.qa.megagenerator.aiAssistant.inners;

public record StateButtonConfig(
        boolean download,
        boolean update,
        boolean remove,
        boolean progress
) { }
