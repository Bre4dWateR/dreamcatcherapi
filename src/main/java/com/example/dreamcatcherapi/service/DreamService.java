// src/main/java/com/example/dreamcatcherapi/service/DreamService.java
package com.example.dreamcatcherapi.service;

import com.example.dreamcatcherapi.entity.Dream;
import com.example.dreamcatcherapi.dto.DreamRequest;
import com.example.dreamcatcherapi.exception.DreamNotFoundException; // Убедитесь, что исключения импортированы, если они используются
import com.example.dreamcatcherapi.exception.UserNotFoundException;

import java.util.List;
import java.util.Optional;

// *** ИЗМЕНЕНИЕ: СДЕЛАЙТЕ ЭТО ИНТЕРФЕЙСОМ ***
public interface DreamService { // Измените 'class' на 'interface'

    // *** ИЗМЕНЕНИЕ: УДАЛИТЕ КОНСТРУКТОР ***
    // Интерфейсы не имеют конструкторов

    // *** ИЗМЕНЕНИЕ: ВСЕ МЕТОДЫ ИНТЕРФЕЙСА ПО УМОЛЧАНИЮ PUBLIC И ABSTRACT ***
    // Не нужны ключевые слова public и abstract (они неявно присутствуют)
    // Также убираем @Transactional здесь, так как оно относится к реализации

    Dream createDream(DreamRequest dreamRequest);

    Optional<Dream> getDreamById(Long id);

    List<Dream> getDreamsByUserId(Long userId);

    Dream updateDream(Long id, DreamRequest dreamRequest);

    // Этот метод был абстрактным в предыдущей версии DreamService
    // Теперь он должен быть частью интерфейса.
    // Если он вам не нужен, можете удалить его.
    // void deleteDream(Long id); // <-- Если вы использовали его, оставьте, но без @Transactional и public abstract

    Optional<Dream> getDreamByIdAndUserId(Long dreamId, Long userId);

    // Метод, который вы вызываете из DreamController
    void deleteDreamByIdAndUserId(Long dreamId, Long userId);
}