package org.app.mss.web.controllers;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import org.app.mss.services.interfaces.IDataService;
import org.app.mss.web.dtos.responses.BaseResponse;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequestMapping("data")
public class DataController {

    private final IDataService service;

    public DataController(IDataService service) {
        this.service = service;
    }

    @GetMapping("get/history/v1/{sensor}")
    public ResponseEntity<BaseResponse> getHistory(@PathVariable @NotBlank String sensor){
        return service.getData(sensor).apply();
    }

    @GetMapping("get/history/v1")
    public ResponseEntity<BaseResponse> getHistoryByDate(@RequestParam @NotBlank String sensor,
                                                         @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) @Valid LocalDate date){
        return service.getData(sensor, date).apply();
    }

}