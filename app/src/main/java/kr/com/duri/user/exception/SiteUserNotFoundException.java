package kr.com.duri.user.exception;

public class SiteUserNotFoundException extends RuntimeException {
    public SiteUserNotFoundException(String message) {
        super(message);
    }
}
