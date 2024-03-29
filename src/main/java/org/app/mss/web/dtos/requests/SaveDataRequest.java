package org.app.mss.web.dtos.requests;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SaveDataRequest {

    private Double temperature;

    private Double temperatureEnvironment;

    private Double humidityEnvironment;

    private Integer day;

    private Integer mount;

    private Integer year;

    private Integer hour;

    private Integer minute;

    private Integer second;

}