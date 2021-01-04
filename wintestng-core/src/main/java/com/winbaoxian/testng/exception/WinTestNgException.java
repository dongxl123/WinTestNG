package com.winbaoxian.testng.exception;

/**
 * @author dongxuanliang252
 * @date 2019-03-07 14:54
 */
public class WinTestNgException extends RuntimeException {

    public WinTestNgException(String message) {
        super(message);
    }

    public WinTestNgException(Throwable cause) {
        super(cause);
    }

    public WinTestNgException(String message, Throwable cause) {
        super(message, cause);
    }
}
