package com.studymate.service;

import com.studymate.entity.StudySession;
import com.studymate.entity.User;
import com.studymate.exception.UserNotFoundException;
import com.studymate.repository.StudySessionRepository;
import com.studymate.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

@Service
@RequiredArgsConstructor
public class StudyStatisticsService {

    private final StudySessionRepository sessionRepository;
    private final UserRepository userRepository;

    public List<Integer> getDailyStudyHours(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(UserNotFoundException::new);

        LocalDateTime startOfDay = LocalDate.now().atStartOfDay();
        LocalDateTime endOfDay = LocalDate.now().atTime(23, 59, 59);

        List<StudySession> sessions = sessionRepository.findAllByUserIdAndStartTimeBetween(
                user.getId(), startOfDay, endOfDay
        );

        int[] hourly = new int[24];
        for (StudySession session : sessions) {
            LocalDateTime start = session.getStartTime();
            LocalDateTime end = session.getEndTime();

            while (!start.isAfter(end)) {
                if (!start.toLocalDate().equals(LocalDate.now())) break;
                int hour = start.getHour();
                hourly[hour] += 1;
                start = start.plusMinutes(1);
            }
        }

        return Arrays.stream(hourly).boxed().toList();
    }

    public Map<String, Integer> getWeeklyStudyHours(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(UserNotFoundException::new);

        LocalDate now = LocalDate.now();
        LocalDateTime startTime = now.minusDays(6).atStartOfDay();
        LocalDateTime endTime = now.atTime(23, 59, 59);

        List<StudySession> sessions = sessionRepository.findAllByUserIdAndStartTimeBetween(
                user.getId(), startTime, endTime
        );

        List<DayOfWeek> weekOrder = List.of(
                DayOfWeek.MONDAY,
                DayOfWeek.TUESDAY,
                DayOfWeek.WEDNESDAY,
                DayOfWeek.THURSDAY,
                DayOfWeek.FRIDAY,
                DayOfWeek.SATURDAY,
                DayOfWeek.SUNDAY
        );

        Map<String, Integer> result = new LinkedHashMap<>();
        for (DayOfWeek day : weekOrder) {
            result.put(day.toString(), 0);
        }

        for (StudySession session : sessions) {
            DayOfWeek day = session.getStartTime().getDayOfWeek();
            String key = day.toString();
            result.put(key, result.getOrDefault(key, 0) + session.getDuration());
        }

        return result;
    }

    public Map<String, Integer> getStudyTimeByTag(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(UserNotFoundException::new);

        List<StudySession> sessions = sessionRepository.findAllByUserId(user.getId());

        Map<String, Integer> result = new HashMap<>();
        for (StudySession s : sessions) {
            String tagName = s.getTag().getName();
            result.put(tagName, result.getOrDefault(tagName, 0) + s.getDuration());
        }

        return result;
    }
}