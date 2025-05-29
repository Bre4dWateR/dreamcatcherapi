// src/main/java/com/example/dreamcatcherapi/controller/DreamController.java
package com.example.dreamcatcherapi.controller;

import com.example.dreamcatcherapi.entity.Dream;
import com.example.dreamcatcherapi.entity.User;
import com.example.dreamcatcherapi.service.DreamService;
import com.example.dreamcatcherapi.service.UserService; // Для получения User
import com.example.dreamcatcherapi.dto.DreamRequest; // Импортируем DreamRequest
import com.example.dreamcatcherapi.exception.DreamNotFoundException;
import com.example.dreamcatcherapi.exception.UserNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/dreams")
public class DreamController {

    private final DreamService dreamService;
    private final UserService userService; // Внедряем UserService

    @Autowired
    public DreamController(DreamService dreamService, UserService userService) {
        this.dreamService = dreamService;
        this.userService = userService;
    }

    @PostMapping
    public ResponseEntity<Dream> createDream(@RequestBody DreamRequest dreamRequest) {
        // Проверка существования пользователя должна быть в сервисе или контроллере.
        // Здесь мы можем просто передать DreamRequest в сервис,
        // а сервис позаботится о поиске пользователя и маппинге.
        try {
            Dream createdDream = dreamService.createDream(dreamRequest);
            return new ResponseEntity<>(createdDream, HttpStatus.CREATED);
        } catch (UserNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (IllegalArgumentException e) {
            // Например, неверный формат даты
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<Dream> getDreamById(@PathVariable Long id) {
        Optional<Dream> dream = dreamService.getDreamById(id);
        return dream.map(value -> new ResponseEntity<>(value, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Dream>> getDreamsByUserId(@PathVariable Long userId) {
        try {
            List<Dream> dreams = dreamService.getDreamsByUserId(userId);
            return new ResponseEntity<>(dreams, HttpStatus.OK);
        } catch (UserNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Dream> updateDream(@PathVariable Long id, @RequestBody DreamRequest dreamRequest) {
        try {
            Dream updatedDream = dreamService.updateDream(id, dreamRequest);
            return new ResponseEntity<>(updatedDream, HttpStatus.OK);
        } catch (DreamNotFoundException | UserNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (IllegalArgumentException e) {
            // Например, неверный формат даты или несоответствие владельца
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("/{dreamId}/user/{userId}") // <-- ОБНОВИТЕ ЭТУ АННОТАЦИЮ
    public ResponseEntity<Void> deleteDream(
            @PathVariable Long dreamId,  // <-- Используем dreamId
            @PathVariable Long userId) { // <-- Добавляем userId
        try {
            // Вызываем сервис для удаления сна по dreamId и userId
            dreamService.deleteDreamByIdAndUserId(dreamId, userId); // Предполагаем, что у вас есть такой метод в DreamService
            return new ResponseEntity<>(HttpStatus.NO_CONTENT); // 204 No Content - успешно удалено
        } catch (DreamNotFoundException | UserNotFoundException e) { // Обработка исключений
            return new ResponseEntity<>(HttpStatus.NOT_FOUND); // 404 Not Found, если сон или пользователь не найден
        } catch (Exception e) { // Общая обработка ошибок
            // Логируем ошибку, если что-то пошло не так на сервере
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR); // 500 Internal Server Error
        }
    }

    @GetMapping("/{dreamId}/user/{userId}")
    public ResponseEntity<Dream> getDreamByIdAndUserId(@PathVariable Long dreamId, @PathVariable Long userId) {
        try {
            Optional<Dream> dream = dreamService.getDreamByIdAndUserId(dreamId, userId);
            return dream.map(value -> new ResponseEntity<>(value, HttpStatus.OK))
                    .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
        } catch (DreamNotFoundException e) { // Добавил, если сервис может бросить это исключение
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}
