package com.studymate.dto.studysession;

import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class StudySessionRequest {
    private Long tagId;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private String memo;

    public StudySessionRequest(Long tagId, LocalDateTime startTime, LocalDateTime endTime, String memo) {
        this.tagId = tagId;
        this.startTime = startTime;
        this.endTime = endTime;
        this.memo = memo;
    }
}
