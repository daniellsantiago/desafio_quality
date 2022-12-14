package com.grupo2.desafioquality.exception;

import com.grupo2.desafioquality.dto.ErrorMessageResponseDto;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
@AllArgsConstructor
public class GlobalExceptionHandler {

    private final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseBody
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorMessageResponseDto handleMethodArgumentNotValidException(MethodArgumentNotValidException exception) {
        logger.error("MethodArgumentNotValidException: ", exception);
        return ErrorMessageResponseDto.withFieldErrors(exception.getBindingResult().getFieldErrors());
    }

    @ExceptionHandler(NotFoundException.class)
    @ResponseBody
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorMessageResponseDto handleNotFoundException(NotFoundException exception) {
        logger.error("NotFoundException: ", exception);
        return ErrorMessageResponseDto.of(exception.getMessage(), "NOT_FOUND_ERROR");
    }

    @ExceptionHandler(Exception.class)
    @ResponseBody
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorMessageResponseDto handleException(Exception exception) {
        logger.error("Exception: ", exception);
        return ErrorMessageResponseDto.of("An unknown server error has occurred", "UNKNOWN_SERVER_ERROR");
    }
}
