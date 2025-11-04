package ru.qa.megagenerator.features.common.interfaces;

public interface AIProvider {

    String getName();
    String generateCode(String prompt);
    String optimizeCode(String source);
    String explainCode(String source);
    String send(String prompt);

}
