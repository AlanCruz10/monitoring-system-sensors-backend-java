package org.app.mss.web.controllers;

import jakarta.validation.Valid;
import org.app.mss.services.interfaces.IDataService;
import org.app.mss.web.dtos.responses.BaseResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("data")
public class DataController {

    private final IDataService service;

    public DataController(IDataService service) {
        this.service = service;
    }

    @GetMapping("get/history/v1/{sensor}")
    public ResponseEntity<BaseResponse> getHistory(@PathVariable @Valid String sensor){
        return service.getData(sensor).apply();
    }

}