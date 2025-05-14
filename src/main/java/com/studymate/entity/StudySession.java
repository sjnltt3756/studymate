package com.studymate.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "study_sessions")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StudySession {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDateTime startTime;

    private LocalDateTime endTime;

    private Integer duration;

    @Lob
    private String memo;

    private LocalDateTime createdAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tag_id", nullable = false)
    private Tag tag;

    public static StudySession create(User user, Tag tag, LocalDateTime startTime,
                                      LocalDateTime endTime, Integer duration, String memo) {
        StudySession session = new StudySession();
        session.user = user;
        session.tag = tag;
        session.startTime = startTime;
        session.endTime = endTime;
        session.duration = duration;
        session.memo = memo;
        session.createdAt = LocalDateTime.now();
        return session;
    }
}
