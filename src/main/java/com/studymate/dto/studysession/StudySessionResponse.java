package com.studymate.dto.studysession;

import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class StudySessionResponse {
    private final Long id;
    private final String tagName;
    private final int duration; // 분 단위
    private final String memo;
    private final LocalDateTime startTime;
    private final LocalDateTime endTime;

    public StudySessionResponse(Long id, String tagName, int duration, String memo,
                                LocalDateTime startTime, LocalDateTime endTime) {
        this.id = id;
        this.tagName = tagName;
        this.duration = duration;
        this.memo = memo;
        this.startTime = startTime;
        this.endTime = endTime;
    }
}
