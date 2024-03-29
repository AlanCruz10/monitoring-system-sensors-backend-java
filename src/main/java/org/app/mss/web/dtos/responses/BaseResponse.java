package org.app.mss.web.dtos.responses;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@Builder
@Getter
@Setter
public class BaseResponse {

    private Object data;

    private String message;

    private Boolean success;

    private Integer statusCode;

    private HttpStatus httpStatus;

    public ResponseEntity<BaseResponse> apply() {
        return new ResponseEntity<>(this, httpStatus);
    }

}