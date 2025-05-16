package com.studymate.service;

import com.studymate.dto.studysession.StudySessionRequest;
import com.studymate.dto.studysession.StudySessionResponse;
import com.studymate.entity.StudySession;
import com.studymate.entity.Tag;
import com.studymate.entity.User;
import com.studymate.repository.StudySessionRepository;
import com.studymate.repository.TagRepository;
import com.studymate.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.List;

@Service
@RequiredArgsConstructor
public class StudySessionService {

    private final StudySessionRepository sessionRepository;
    private final UserRepository userRepository;
    private final TagRepository tagRepository;

    public void saveSession(String username, StudySessionRequest request) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("유저를 찾을 수 없습니다."));
        Tag tag = tagRepository.findById(request.getTagId())
                .orElseThrow(() -> new IllegalArgumentException("태그를 찾을 수 없습니다."));

        int duration = (int) Duration.between(request.getStartTime(), request.getEndTime()).toMinutes();

        StudySession session = StudySession.create(
                user, tag, request.getStartTime(), request.getEndTime(), duration, request.getMemo()
        );

        sessionRepository.save(session);
    }

    public List<StudySessionResponse> getUserSessions(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("유저를 찾을 수 없습니다."));

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
}
