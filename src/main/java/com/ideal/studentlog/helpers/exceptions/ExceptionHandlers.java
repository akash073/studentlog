package com.ideal.studentlog.helpers.exceptions;

import com.ideal.studentlog.helpers.dataclass.ErrorResponseDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.stream.Collectors;

@Slf4j
@RestControllerAdvice
public class ExceptionHandlers {

    @ExceptionHandler(ServiceException.class)
    public ResponseEntity<ErrorResponseDTO> handleServiceException(ServiceException exception) {
        log.info(exception.getMessage());
        return new ResponseEntity<>(exception.getResponseDTO(), exception.getStatus());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponseDTO> handleInvalidArgumentException(MethodArgumentNotValidException exception) {
        HttpStatus status = HttpStatus.BAD_REQUEST;

        ErrorResponseDTO responseDTO = new ErrorResponseDTO(
                status.getReasonPhrase(),
                exception
                        .getBindingResult()
                        .getAllErrors()
                        .stream()
                        .filter(error -> error instanceof FieldError)
                        .map(error -> (FieldError) error)
                        .map(error -> "Invalid value for field " + error.getField() + ": " + error.getRejectedValue())
                        .collect(Collectors.toList())
                        .toString(),
                "API-" + status.value()
        );

        return new ResponseEntity<>(responseDTO, status);
    }

    /*@ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponseDTO> handleException(Exception exception) {
        log.error(exception.getMessage());

        HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;

        ErrorResponseDTO responseDTO = new ErrorResponseDTO(
                status.getReasonPhrase(),
                status.getReasonPhrase(),
                "API-" + status.value()
        );

        return new ResponseEntity<>(responseDTO, status);
    }*/

}
