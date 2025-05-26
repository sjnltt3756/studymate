package com.studymate.exception;

public class TagNotFoundException extends RuntimeException{
    public TagNotFoundException(){
        super("존재하지 않는 태그입니다.");
    }
}
