package org.app.mss.services.impls;

import org.app.mss.persistences.entities.Data;
import org.app.mss.persistences.repositories.IDataRepository;
import org.app.mss.services.interfaces.IDataService;
import org.app.mss.web.dtos.responses.BaseResponse;
import org.app.mss.web.dtos.responses.DataResponse;
import org.app.mss.web.exceptions.NotFoundDataException;
import org.springframework.cglib.core.Local;
import org.springframework.data.relational.core.sql.In;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class DataService implements IDataService {

    private final IDataRepository repository;

    public DataService(IDataRepository repository) {
        this.repository = repository;
    }

    @Override
    public BaseResponse getData(String sensor) {
        return BaseResponse.builder()
                .data(listDataResponse(sensor.toLowerCase(Locale.ROOT).replace(" ", "")))
                .detail("Data sensor " + sensor)
                .success(Boolean.TRUE)
                .status(HttpStatus.OK.value())
                .httpStatus(HttpStatus.OK).build();
    }

    @Override
    public BaseResponse getData(String sensor, LocalDate date) {
        return BaseResponse.builder()
                .data(filterByMeasurementAndByDate(sensor.toLowerCase(Locale.ROOT).replace(" ", ""), date))
                .detail("Data sensor " + sensor)
                .success(Boolean.TRUE)
                .status(HttpStatus.OK.value())
                .httpStatus(HttpStatus.OK).build();
    }

    private List<Data> getAllDataBySensor(String sensor){
        Optional<List<Data>> allBySensor = repository.findAllBySensor(sensor);
        if (allBySensor.get().isEmpty()){
            throw new NotFoundDataException();
        }
        return allBySensor.get();
    }

    private List<DataResponse> listDataResponse(String sensor){
        return getAllDataBySensor(sensor)
                .stream()
                .map(this::from)
                .toList();
    }

    private Map<String, Map<String, List<DataResponse>>> filterByMeasurementAndByDate(String sensor, LocalDate date){
        Map<String, List<DataResponse>> filterByMeasurement = filterByMeasurement(sensor);
        Map<String, Map<String, List<DataResponse>>> filterByMeasurementAndByDate = new HashMap<>();
        filterByMeasurement.forEach((measurement, data)->{
            Map<String, List<DataResponse>> listFilteredByDate = filterByDate(data, date);
            Map<String, List<DataResponse>> average = average(listFilteredByDate, date);
            filterByMeasurementAndByDate.put(measurement, average);
        });
        List<Boolean> validate = new ArrayList<>();
        filterByMeasurementAndByDate.values().forEach(map -> {
            if (map.isEmpty()) {
                validate.add(true);
            }else {
                validate.add(false);
            }
        });
        if (!validate.contains(false)) {
            throw new NotFoundDataException(date);
        }
        return filterByMeasurementAndByDate;
    }

    private Map<String, List<DataResponse>> average(Map<String, List<DataResponse>> filterByDate, LocalDate date) {
        filterByDate.forEach((dateFilter, dataResponses)->{
            if (dateFilter.startsWith("month")) {
                filterByDate.put(dateFilter, averageByMonth(dataResponses, date));
            }
            if (dateFilter.startsWith("year")){
                Map<Integer, Double> averageYearByMonth= new HashMap<>();
                List<DataResponse> newDataMonthByAverage = new ArrayList<>();
                Map<Integer, List<DataResponse>> listFilteredByMonth = new HashMap<>();
                dataResponses.forEach(dataResponse -> {
                    listFilteredByMonth.computeIfAbsent(dataResponse.getDate().getMonthValue(), k -> new ArrayList<>()).add(dataResponse);
                });
                listFilteredByMonth.forEach((month, dataResponseList)->{
                    listFilteredByMonth.put(month, averageByMonth(dataResponseList, date));
                });
                listFilteredByMonth.forEach((month, dataResponseList)->{
                    dataResponseList.forEach(dataResponse -> {
                        averageYearByMonth.put(month, averageYearByMonth.getOrDefault(month, 0.0) + dataResponse.getValue());
                    });
                });
                averageYearByMonth.forEach((month, sums)->{
                    int lengthOfMonth = YearMonth.of(date.getYear(), month).lengthOfMonth();
                    double average = (double) sums / lengthOfMonth;
                    averageYearByMonth.put(month, average);
                });
                DataResponse dataResponse = dataResponses.get(0);
                averageYearByMonth.forEach((month, averages) -> {
                    int year = date.getYear();
                    DataResponse dataMonthResponse = DataResponse.builder()
                            .type(dataResponse.getType())
                            .value(averages)
                            .dateMonth(year + "-" + month)
                            .sensor(dataResponse.getSensor()).build();
                    newDataMonthByAverage.add(dataMonthResponse);
                });
                filterByDate.put(dateFilter, newDataMonthByAverage);
            }
        });
        return filterByDate;
    }

    private List<DataResponse> averageByMonth(List<DataResponse> dataResponses, LocalDate date){
        Map<String, Double> averageMonthByDay = new HashMap<>();
        Map<String, Integer> dailyCount = new HashMap<>();
        List<DataResponse> newDataMonthByAverage = new ArrayList<>();
        dataResponses.forEach(dataResponse -> {
            int dayOfMonth = dataResponse.getDate().getDayOfMonth();
            averageMonthByDay.put(String.valueOf(dayOfMonth), averageMonthByDay.getOrDefault(String.valueOf(dayOfMonth), 0.0) + dataResponse.getValue());
            dailyCount.put(String.valueOf(dayOfMonth), dailyCount.getOrDefault(String.valueOf(dayOfMonth), 0) + 1);
        });
        averageMonthByDay.forEach((days, sums)->{
            Integer i = dailyCount.get(days);
            double average = (double) sums / i;
            averageMonthByDay.put(days, average);
        });
        DataResponse dataResponse = dataResponses.get(0);
        averageMonthByDay.forEach((days, averages) -> {
            int year = date.getYear();
            int monthValue = dataResponse.getDate().getMonthValue();
            int day = Integer.parseInt(days);
            DataResponse dataMonthResponse = DataResponse.builder()
                    .type(dataResponse.getType())
                    .value(averages)
                    .date(LocalDate.parse(String.format("%d-%02d-%02d", year, monthValue, day)))
                    .sensor(dataResponse.getSensor()).build();
            newDataMonthByAverage.add(dataMonthResponse);
        });
        return newDataMonthByAverage;
    }

    private Map<Integer, List<DataResponse>> filterByDate(String sensor, LocalDate date){
        Map<Integer, List<DataResponse>> listFilteredByDate = new HashMap<>();
        listDataResponse(sensor)
                .forEach(dataResponse -> {
                    if (dataResponse.getDate().getDayOfMonth() == date.getDayOfMonth() && dataResponse.getDate().getMonthValue() == date.getMonthValue() && dataResponse.getDate().getYear() == date.getYear()) {
                        listFilteredByDate.computeIfAbsent(date.getDayOfMonth(), k -> new ArrayList<>()).add(dataResponse);
                    }
                    if (dataResponse.getDate().getMonthValue() == date.getMonthValue() && dataResponse.getDate().getYear() == date.getYear()) {
                        listFilteredByDate.computeIfAbsent(date.getMonthValue(), k -> new ArrayList<>()).add(dataResponse);
                    }
                    if (dataResponse.getDate().getYear() == date.getYear()) {
                        listFilteredByDate.computeIfAbsent(date.getYear(), k -> new ArrayList<>()).add(dataResponse);
                    }
                });
        return listFilteredByDate;
    }

    private Map<String, List<DataResponse>> filterByDate(List<DataResponse> listDataResponse, LocalDate date){
        Map<String, List<DataResponse>> listFilteredByDate = new HashMap<>();
        listDataResponse
                .forEach(dataResponse -> {
                    if (dataResponse.getDate().getDayOfMonth() == date.getDayOfMonth() && dataResponse.getDate().getMonthValue() == date.getMonthValue() && dataResponse.getDate().getYear() == date.getYear()) {
                        listFilteredByDate.computeIfAbsent("day_" + date.getDayOfMonth(), k -> new ArrayList<>()).add(dataResponse);
                    }
                    if (dataResponse.getDate().getMonthValue() == date.getMonthValue() && dataResponse.getDate().getYear() == date.getYear()) {
                        listFilteredByDate.computeIfAbsent("month_" + date.getMonthValue(), k -> new ArrayList<>()).add(dataResponse);
                    }
                    if (dataResponse.getDate().getYear() == date.getYear()) {
                        listFilteredByDate.computeIfAbsent("year_" + date.getYear(), k -> new ArrayList<>()).add(dataResponse);
                    }
                });
        return listFilteredByDate;
    }

    private Map<String, List<DataResponse>> filterByMeasurement(String sensor){
        Map<String, List<DataResponse>> listTypeDataBySensor = new HashMap<>();
        listDataResponse(sensor)
                .forEach(dataResponse -> {
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