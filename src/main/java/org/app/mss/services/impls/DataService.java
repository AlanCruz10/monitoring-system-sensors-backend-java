package org.app.mss.services.impls;

import org.app.mss.persistences.entities.Data;
import org.app.mss.persistences.repositories.IDataRepository;
import org.app.mss.services.interfaces.IDataService;
import org.app.mss.web.dtos.requests.SaveDataRequest;
import org.app.mss.web.dtos.responses.BaseResponse;
import org.app.mss.web.dtos.responses.DataResponse;
import org.app.mss.web.exceptions.NotFoundDataException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class DataService implements IDataService {

    private final IDataRepository repository;

    public DataService(IDataRepository repository) {
        this.repository = repository;
    }

    @Override
    public BaseResponse saveData(SaveDataRequest request) {
        return BaseResponse.builder()
                .data(from(repository.save(from(request))))
                .message("Save Data Correctly")
                .success(Boolean.TRUE)
                .statusCode(HttpStatus.OK.value())
                .httpStatus(HttpStatus.OK).build();
    }

    @Override
    public BaseResponse getData(String sensor) {
        return BaseResponse.builder()
                .data(getDataResponse(sensor))
                .message("Data sensor " + sensor)
                .success(Boolean.TRUE)
                .statusCode(HttpStatus.OK.value())
                .httpStatus(HttpStatus.OK).build();
    }

    private List<Data> getDataListBySensor(String sensor){
        return repository.findAllBySensor(sensor).orElseThrow(NotFoundDataException::new);
    }

    private List<DataResponse> getDataResponse(String sensor){
        return getDataListBySensor(sensor)
                .stream()
                .map(this::from)
                .collect(Collectors.toList());
    }

    private Data from(SaveDataRequest request){
        Data data = new Data();
        data.setDay(request.getDay());
        data.setMount(request.getMount());
        data.setYear(request.getYear());
        data.setHour(request.getHour());
        data.setMinute(request.getMinute());
        data.setSecond(request.getSecond());
        return data;
    }

    private DataResponse from(Data data){
        return DataResponse.builder()
                .id(data.getId())
                .day(data.getDay())
                .hour(data.getHour())
                .year(data.getYear())
                .minute(data.getMinute())
                .mount(data.getMount())
                .second(data.getSecond())
                .type(data.getType())
                .sensor(data.getSensor())
                .value(data.getValor()).build();
    }

}