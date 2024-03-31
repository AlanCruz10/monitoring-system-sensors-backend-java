package org.app.mss.web.dtos.responses;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalTime;

@Getter
@Setter
@Builder
public class DataResponse {

    private Long id;

    private Double value;

    private String sensor;

    private String type;

    private LocalDate date;

    private LocalTime time;

}