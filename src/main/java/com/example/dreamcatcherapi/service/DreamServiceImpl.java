// src/main/java/com/example/dreamcatcherapi/service/DreamServiceImpl.java
package com.example.dreamcatcherapi.service;

import com.example.dreamcatcherapi.entity.Dream;
import com.example.dreamcatcherapi.entity.User;
import com.example.dreamcatcherapi.repository.DreamRepository;
import com.example.dreamcatcherapi.repository.UserRepository;
import com.example.dreamcatcherapi.exception.UserNotFoundException;
import com.example.dreamcatcherapi.exception.DreamNotFoundException;
import com.example.dreamcatcherapi.dto.DreamRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Optional;

@Service
// *** ИЗМЕНЕНИЕ: ИСПОЛЬЗУЙТЕ 'implements' ВМЕСТО 'extends' ***
public class DreamServiceImpl implements DreamService { // Измените 'extends' на 'implements'

    private final DreamRepository dreamRepository;
    private final UserRepository userRepository;

    @Autowired
    public DreamServiceImpl(DreamRepository dreamRepository, UserRepository userRepository) {
        this.dreamRepository = dreamRepository;
        this.userRepository = userRepository;
    }

    @Override
    @Transactional
    public Dream createDream(DreamRequest dreamRequest) {
        User user = userRepository.findById(dreamRequest.getUserId())
                .orElseThrow(() -> new UserNotFoundException("User not found with ID: " + dreamRequest.getUserId()));

        Dream dream = new Dream();
        dream.setUser(user);
        dream.setTitle(dreamRequest.getTitle());
        dream.setDescription(dreamRequest.getStory());

        try {
            dream.setDreamDate(LocalDate.parse(dreamRequest.getDate()));
        } catch (DateTimeParseException e) {
            throw new IllegalArgumentException("Invalid date format. Expected YYYY-MM-DD.", e);
        }

        return dreamRepository.save(dream);
    }

    @Override
    public Optional<Dream> getDreamById(Long id) {
        return dreamRepository.findById(id);
    }

    @Override
    public List<Dream> getDreamsByUserId(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found with ID: " + userId));
        return dreamRepository.findByUser(user);
    }

    @Override
    @Transactional
    public Dream updateDream(Long id, DreamRequest dreamRequest) {
        Dream existingDream = dreamRepository.findById(id)
                .orElseThrow(() -> new DreamNotFoundException("Dream not found with ID: " + id));

        if (!existingDream.getUser().getId().equals(dreamRequest.getUserId())) {
            throw new IllegalArgumentException("User ID in request does not match dream owner.");
        }

        existingDream.setTitle(dreamRequest.getTitle());
        existingDream.setDescription(dreamRequest.getStory());

        try {
            existingDream.setDreamDate(LocalDate.parse(dreamRequest.getDate()));
        } catch (DateTimeParseException e) {
            throw new IllegalArgumentException("Invalid date format. Expected YYYY-MM-DD.", e);
        }

        return dreamRepository.save(existingDream);
    }

    // *** ИЗМЕНЕНИЕ: УДАЛИТЕ ИЛИ ПЕРЕИМЕНУЙТЕ ЭТОТ МЕТОД, ЕСЛИ ОН НЕ ТРЕБУЕТСЯ ИНТЕРФЕЙСОМ ***
    // В текущей структуре вы, вероятно, используете deleteDreamByIdAndUserId
    // Если вам нужна эта сигнатура, добавьте ее в интерфейс DreamService
    // Если нет, удалите ее отсюда.
    /*
    @Transactional
    @Override // <--- Здесь будет ошибка, если его нет в интерфейсе
    public void deleteDream(Long id) {
        if (!dreamRepository.existsById(id)) {
            throw new DreamNotFoundException("Dream with ID: " + id + " not found for deletion.");
        }
        dreamRepository.deleteById(id);
    }
    */

    @Override // <--- Теперь должен быть корректным, если есть в интерфейсе
    public Optional<Dream> getDreamByIdAndUserId(Long dreamId, Long userId) {
        // Убедитесь, что в DreamRepository есть метод findByIdAndUser_Id(Long id, Long userId);
        // Если нет, то нужно его добавить
        return dreamRepository.findByIdAndUser_Id(dreamId, userId);
    }

    @Override // <--- Теперь должен быть корректным, если есть в интерфейсе
    @Transactional
    public void deleteDreamByIdAndUserId(Long dreamId, Long userId) {
        Dream dreamToDelete = dreamRepository.findById(dreamId)
                .orElseThrow(() -> new DreamNotFoundException("Dream not found with id: " + dreamId));

        if (!dreamToDelete.getUser().getId().equals(userId)) {
            throw new IllegalArgumentException("Dream does not belong to the specified user.");
        }

        dreamRepository.delete(dreamToDelete);
    }
}


