package kr.com.duri.common;

import kr.com.duri.common.exception.BadRequestException;
import kr.com.duri.common.exception.IllegalParameterException;
import kr.com.duri.common.exception.NotFoundException;
import kr.com.duri.common.response.CommonResponseEntity;
import kr.com.duri.common.response.CustomError;

import kr.com.duri.groomer.exception.ShopNotFoundException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class CommonControllerAdvice {

    private ResponseEntity<CommonResponseEntity<Object>> response(
            Throwable throwable, HttpStatus status) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        return new ResponseEntity<>(
                new CommonResponseEntity<>(
                        false, null, new CustomError(throwable.getMessage(), status)),
                headers,
                status);
    }

    @ExceptionHandler({IllegalParameterException.class, BadRequestException.class})
    public ResponseEntity<?> handleBadRequestException(Exception e) {
        return response(e, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<?> handleExceptionForNotFound(Exception e) {
        return response(e, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(ShopNotFoundException.class)
    public ResponseEntity<?> handleShopNotFoundException(ShopNotFoundException e) {
        return response(e, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> handleGlobalException(Exception e) {
        return response(e, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
