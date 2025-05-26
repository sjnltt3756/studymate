
package com.studymate.exception;

public class StudySessionNotFoundException extends RuntimeException{
    public StudySessionNotFoundException(){
        super("기록이 존재하지 않습니다.");
    }
}
