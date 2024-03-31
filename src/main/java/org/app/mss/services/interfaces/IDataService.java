package org.app.mss.services.interfaces;

import org.app.mss.web.dtos.responses.BaseResponse;

public interface IDataService {

    BaseResponse getData(String sensor);

}