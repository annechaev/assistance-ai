package ru.qa.megagenerator.aiAssistant.clients;

public class AiClient {

    public static String send(String prompt) {
        System.out.println("Запрос %s".formatted(prompt));
        return "Hello";
    }

}
