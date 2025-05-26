package com.studymate.service;

import com.studymate.dto.studysession.StudySessionRequest;
import com.studymate.dto.studysession.StudySessionResponse;
import com.studymate.entity.StudySession;
import com.studymate.entity.Tag;
import com.studymate.entity.User;
import com.studymate.exception.ForbiddenException;
import com.studymate.exception.StudySessionNotFoundException;
import com.studymate.exception.TagNotFoundException;
import com.studymate.exception.UserNotFoundException;
import com.studymate.repository.StudySessionRepository;
import com.studymate.repository.TagRepository;
import com.studymate.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class StudySessionService {

    private final StudySessionRepository sessionRepository;
    private final UserRepository userRepository;
    private final TagRepository tagRepository;
    private final OpenAIService openAIService;

    public void saveSession(String username, StudySessionRequest request) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(UserNotFoundException::new);
        Tag tag = tagRepository.findById(request.getTagId())
                .orElseThrow(TagNotFoundException::new);

        int duration = (int) Duration.between(request.getStartTime(), request.getEndTime()).toMinutes();

        StudySession session = StudySession.create(
                user, tag, request.getStartTime(), request.getEndTime(), duration, request.getMemo()
        );

        sessionRepository.save(session);
    }

    public List<StudySessionResponse> getUserSessions(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(UserNotFoundException::new);

        return sessionRepository.findAllByUserIdOrderByStartTimeDesc(user.getId())
                .stream()
                .map(s -> new StudySessionResponse(
                        s.getId(),
                        s.getTag().getName(),
                        s.getDuration(),
                        s.getMemo(),
                        s.getStartTime(),
                        s.getEndTime()
                ))
                .toList();
    }

    public List<String> getMemosByDate(String username, LocalDate date) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(UserNotFoundException::new);

        List<StudySession> sessions = sessionRepository.findAllByUserIdAndStartTimeBetween(
                user.getId(), date.atStartOfDay(), date.atTime(23, 59, 59)
        );

        return sessions.stream()
                .map(StudySession::getMemo)
                .filter(memo -> memo != null && !memo.isBlank())
                .toList();
    }

    @Transactional
    public void updateMemo(String username, Long sessionId, String newMemo) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(UserNotFoundException::new);

        StudySession session = sessionRepository.findById(sessionId)
                .orElseThrow(StudySessionNotFoundException::new);

        if (!session.getUser().getId().equals(user.getId())) {
            throw new ForbiddenException("본인의 기록만 수정할 수 있습니다.");
        }

        session.updateMemo(newMemo);
    }

    public String summarizeMemo(Long sessionId) {
        StudySession session = sessionRepository.findById(sessionId)
                .orElseThrow(StudySessionNotFoundException::new);

        String memo = session.getMemo();
        if (memo == null || memo.isBlank()) {
            return "요약할 회고가 없습니다.";
        }

        return openAIService.summarizeText(memo);
    }
}