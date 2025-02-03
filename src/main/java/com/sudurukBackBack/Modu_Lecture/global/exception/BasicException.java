package com.sudurukBackBack.Modu_Lecture.global.exception;

import lombok.Getter;

@Getter
public abstract class BasicException extends RuntimeException {

    abstract public int statusCode();
    abstract public String errorMessage();

}
