package kr.com.duri.common.response;

import org.springframework.http.HttpStatus;

public enum ErrorCode {
    BAD_REQUEST("Invalid request parameters", HttpStatus.BAD_REQUEST),
    NOT_FOUND("Resource not found", HttpStatus.NOT_FOUND),
    ILLEGAL_PARAMETER("Illegal parameter provided", HttpStatus.BAD_REQUEST),
    INTERNAL_SERVER_ERROR("Internal server error", HttpStatus.INTERNAL_SERVER_ERROR);

    private final String message;
    private final HttpStatus status;

    ErrorCode(String message, HttpStatus status) {
        this.message = message;
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public HttpStatus getStatus() {
        return status;
    }
}
