package com.studymate.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "tags")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Tag {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @OneToMany(mappedBy = "tag", cascade = CascadeType.ALL)
    private List<StudySession> studySessions = new ArrayList<>();

    // 정적 팩토리 메서드
    public static Tag create(String name, User user) {
        Tag tag = new Tag();
        tag.name = name;
        tag.user = user;
        return tag;
    }


}
