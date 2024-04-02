package org.app.mss.services.interfaces;

import org.app.mss.web.dtos.responses.BaseResponse;

import java.time.LocalDate;

public interface IDataService {

    BaseResponse getData(String sensor);

    BaseResponse getData(String sensor, LocalDate date);

}