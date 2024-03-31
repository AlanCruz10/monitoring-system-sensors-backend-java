package org.app.mss.services.impls;

import org.app.mss.persistences.entities.Data;
import org.app.mss.persistences.repositories.IDataRepository;
import org.app.mss.services.interfaces.IDataService;
import org.app.mss.web.dtos.responses.BaseResponse;
import org.app.mss.web.dtos.responses.DataResponse;
import org.app.mss.web.exceptions.NotFoundDataException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class DataService implements IDataService {

    private final IDataRepository repository;

    public DataService(IDataRepository repository) {
        this.repository = repository;
    }

    @Override
    public BaseResponse getData(String sensor) {
        return BaseResponse.builder()
                .data(getDataResponse(sensor.toLowerCase(Locale.ROOT).replace(" ", "")))
                .message("Data sensor " + sensor)
                .success(Boolean.TRUE)
                .statusCode(HttpStatus.OK.value())
                .httpStatus(HttpStatus.OK).build();
    }

    private List<Data> getDataListBySensor(String sensor){
        return repository.findAllBySensor(sensor).orElseThrow(NotFoundDataException::new);
    }

    private Map<String, List<DataResponse>> getDataResponse(String sensor){
        Map<String, List<DataResponse>> listTypeDataBySensor = new HashMap<>();
        getDataListBySensor(sensor)
                .stream()
                .map(this::from)
                .toList().forEach(dataResponse -> {
                    String type = dataResponse.getType();
                    listTypeDataBySensor.computeIfAbsent(type, k -> new ArrayList<>()).add(dataResponse);
                });
        return listTypeDataBySensor;
    }

    private DataResponse from(Data data){
        return DataResponse.builder()
                .id(data.getId())
                .date(data.getDate())
                .time(data.getTime())
                .type(data.getType())
                .sensor(data.getSensor())
                .value(data.getValue()).build();
    }

}