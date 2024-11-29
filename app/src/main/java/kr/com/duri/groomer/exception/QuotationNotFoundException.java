package kr.com.duri.groomer.exception;

public class QuotationNotFoundException extends RuntimeException {
    public QuotationNotFoundException(String message) {
        super(message);
    }
}
