package com.studymate.repository;

import com.studymate.entity.StudySession;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public interface StudySessionRepository extends JpaRepository<StudySession, Long> {
    List<StudySession> findAllByUserIdOrderByStartTimeDesc(Long userId);
    List<StudySession> findAllByUserIdAndStartTimeBetween(Long userId, LocalDateTime start, LocalDateTime end);
    List<StudySession> findAllByUserId(Long id);
}
