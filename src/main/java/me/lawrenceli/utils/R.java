package me.lawrenceli.utils;

import java.io.Serial;
import java.io.Serializable;

public class R<T> implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    private boolean success;
    private String msg;
    private T data;

    private R() {
    }

    private R(boolean success, T data, String msg) {
        this.msg = msg;
        this.data = data;
        this.success = success;
    }

    public static <T> R<T> success(T data) {
        return R.result(true, data, null);
    }

    public static <T> R<T> success(T data, String msg) {
        return R.result(true, data, msg);
    }

    public static <T> R<T> fail(String msg) {
        return R.result(false, null, msg);
    }

    public static <T> R<T> fail(T data, String msg) {
        return R.result(false, data, msg);
    }

    private static <T> R<T> result(boolean success, T data, String msg) {
        return new R<>(success, data, msg);
    }

    // getter & setter
    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
