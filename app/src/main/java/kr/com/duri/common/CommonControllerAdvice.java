package kr.com.duri.common;

import kr.com.duri.common.exception.BadRequestException;
import kr.com.duri.common.exception.IllegalParameterException;
import kr.com.duri.common.exception.NotFoundException;
import kr.com.duri.common.response.CommonResponseEntity;
import kr.com.duri.common.response.CustomError;
import kr.com.duri.groomer.exception.GroomerNotFoundException;
import kr.com.duri.groomer.exception.QuotationExistsException;
import kr.com.duri.groomer.exception.QuotationNotFoundException;
import kr.com.duri.groomer.exception.ShopNotFoundException;
import kr.com.duri.user.exception.PetNotFoundException;
import kr.com.duri.user.exception.RequestNotFoundException;
import kr.com.duri.user.exception.ReviewImageUploadException;
import kr.com.duri.user.exception.ReviewNotFoundException;

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

    @ExceptionHandler(GroomerNotFoundException.class)
    public ResponseEntity<?> handleGroomerNotFoundException(GroomerNotFoundException e) {
        return response(e, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(RequestNotFoundException.class)
    public ResponseEntity<?> handleRequestNotFoundException(RequestNotFoundException e) {
        return response(e, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(QuotationNotFoundException.class)
    public ResponseEntity<?> handleQuotationNotFoundException(QuotationNotFoundException e) {
        return response(e, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(PetNotFoundException.class)
    public ResponseEntity<?> handlePetNotFoundException(PetNotFoundException e) {
        return response(e, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(QuotationExistsException.class)
    public ResponseEntity<?> handleQuotationAlreadyExistsException(QuotationExistsException e) {
        return response(e, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(ReviewNotFoundException.class)
    public ResponseEntity<?> handleReviewNotFoundException(ReviewNotFoundException e) {
        return response(e, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> handleGlobalException(Exception e) {
        return response(e, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(ReviewImageUploadException.class)
    public ResponseEntity<?> handleReviewImageUploadException(ReviewImageUploadException e) {
        return response(e, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
