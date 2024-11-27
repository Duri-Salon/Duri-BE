package kr.com.duri.groomer.exception;

public class QuotationExistsException extends RuntimeException {
    public QuotationExistsException(String message) {
        super(message);
    }
}
