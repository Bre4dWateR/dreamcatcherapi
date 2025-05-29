// src/main/java/com/example/dreamcatcherapi/repository/DreamRepository.java
package com.example.dreamcatcherapi.repository;

import com.example.dreamcatcherapi.entity.Dream;
import com.example.dreamcatcherapi.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


import java.util.List;
import java.util.Optional;

@Repository
public interface DreamRepository extends JpaRepository<Dream, Long> {
    List<Dream> findByUser(User user);

    // Добавьте этот метод, если его нет
    Optional<Dream> findByIdAndUser_Id(Long id, Long userId);
}