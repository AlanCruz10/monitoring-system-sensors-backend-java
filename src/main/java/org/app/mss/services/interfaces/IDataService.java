package org.app.mss.services.interfaces;

import org.app.mss.web.dtos.requests.SaveDataRequest;
import org.app.mss.web.dtos.responses.BaseResponse;

public interface IDataService {

    BaseResponse saveData(SaveDataRequest request);

    BaseResponse getData(String sensor);

}