package org.app.mss.web.dtos.responses;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@Builder
@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class BaseResponse {

    private Object data;

    private String detail;

    private Boolean success;

    private Integer status;

    private HttpStatus httpStatus;

    public ResponseEntity<BaseResponse> apply() {
        return new ResponseEntity<>(this, httpStatus);
    }

}