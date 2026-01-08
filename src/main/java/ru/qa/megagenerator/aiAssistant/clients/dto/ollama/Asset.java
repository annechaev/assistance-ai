package ru.qa.megagenerator.aiAssistant.clients.dto.ollama;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonAutoDetect(
        fieldVisibility = JsonAutoDetect.Visibility.ANY, // Читать/писать в любые поля
        getterVisibility = JsonAutoDetect.Visibility.NONE, // Игнорировать геттеры
        setterVisibility = JsonAutoDetect.Visibility.NONE  // Игнорировать сеттеры
)
public class Asset {

    @JsonProperty("size")
    private Double size;

    public Asset() {
    }

    public Double getSize() {
        return size;
    }

    public void setSize(Double size) {
        this.size = size;
    }
}
