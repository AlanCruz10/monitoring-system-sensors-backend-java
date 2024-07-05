package org.app.mss.web.controllers.advices;

import org.app.mss.web.dtos.responses.BaseResponse;
import org.app.mss.web.exceptions.BadRequestException;
import org.app.mss.web.exceptions.NotFoundDataException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ExceptionHandlerFactory {

    @ExceptionHandler(NotFoundDataException.class)
    private ResponseEntity<BaseResponse> handleNotFoundException(NotFoundDataException exception) {
        return BaseResponse.builder()
                .message(exception.getLocalizedMessage())
                .success(Boolean.FALSE)
                .httpStatus(HttpStatus.NOT_FOUND)
                .status(404)
                .build().apply();
    }

    @ExceptionHandler(BadRequestException.class)
    private ResponseEntity<BaseResponse> handleNotFoundException(BadRequestException exception) {
        return BaseResponse.builder()
                .message(exception.getLocalizedMessage())
                .success(Boolean.FALSE)
                .httpStatus(HttpStatus.BAD_REQUEST)
                .status(400)
                .build().apply();
    }

}