package com.studymate.dto.tag;

import lombok.Getter;

@Getter
public class TagRequest {
    private final String name;

    public TagRequest(String name) {
        this.name = name;
    }
}
