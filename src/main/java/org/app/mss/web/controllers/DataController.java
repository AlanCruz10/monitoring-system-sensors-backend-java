package org.app.mss.web.controllers;

import jakarta.validation.Valid;
import org.app.mss.services.interfaces.IDataService;
import org.app.mss.web.dtos.requests.SaveDataRequest;
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

    @PostMapping("save/v1")
    public ResponseEntity<BaseResponse> save(@RequestBody SaveDataRequest request){
        return service.saveData(request).apply();
    }

    @GetMapping("get/history/v1/{sensor}")
    public ResponseEntity<BaseResponse> getHistory(@PathVariable @Valid String sensor){
        return service.getData(sensor).apply();
    }

}