package ru.qa.megagenerator.aiAssistant.items;

public class BubbleMessageItem {

    private Boolean isCode;
    private String message;

    public BubbleMessageItem(String message, Boolean isCode) {
        this.isCode = isCode;
        this.message = message;
    }

    public Boolean getIsCode() {
        return isCode;
    }

    public String getMessage() {
        return message;
    }
}
