package com.example.likelionhackathon.exception;

public class InvalidPasswordException extends RuntimeException {

    public InvalidPasswordException() {
        super("아이디 또는 비밀번호가 올바르지 않습니다.");
    }
}