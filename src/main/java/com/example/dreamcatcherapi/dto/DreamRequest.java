// com.example.dreamcatcherapi.dto.DreamRequest.java (Создайте новую папку 'dto')
package com.example.dreamcatcherapi.dto;

import lombok.Data;
import java.time.LocalDate; // Для LocalDate

@Data // Аннотация Lombok
public class DreamRequest {
    private Long id; // Может быть null для создания, заполнен для обновления
    private Long userId; // Принимаем userId от клиента
    private String title;
    private String story; // Соответствует полю на клиенте
    private String date; // Принимаем дату как строку от клиента
}
