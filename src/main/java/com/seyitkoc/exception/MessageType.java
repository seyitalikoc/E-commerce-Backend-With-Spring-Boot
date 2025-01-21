package com.seyitkoc.exception;

import lombok.Getter;

@Getter
public enum MessageType {

    NO_RECORD_EXIST("1001", "Kayıt bulunamadı."),
    GENERAL_EXCEPTION("9999", "Genel bir hata oluştu."),
    INVALID_CREDENTIALS("1002", "Yanlış email veya şifre."),
    AUTHENTICATION_FAILED("1003","Authentication failed.");

    private String code;

    private String message;

    MessageType(String code, String message) {
        this.code = code;
        this.message = message;
    }
}
