package me.lawrenceli.config.exception;

public class CheckException extends RuntimeException {

    private final String detail;

    public CheckException(String detail) {
        this.detail = detail;
    }

    public String getDetail() {
        return detail;
    }
}
