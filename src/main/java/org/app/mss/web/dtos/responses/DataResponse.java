package org.app.mss.web.dtos.responses;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class DataResponse {

    private Long id;

    private Double value;

    private String sensor;

    private String type;

    private Integer day;

    private Integer mount;

    private Integer year;

    private Integer hour;

    private Integer minute;

    private Integer second;

}