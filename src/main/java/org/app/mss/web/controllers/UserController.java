package org.app.mss.web.controllers;

import jakarta.validation.Valid;
import org.app.mss.services.interfaces.IUserService;
import org.app.mss.web.dtos.requests.CreateUserRequest;
import org.app.mss.web.dtos.requests.LoginUserRequest;
import org.app.mss.web.dtos.responses.BaseResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("user")
public class UserController {

    private final IUserService service;

    public UserController(IUserService service) {
        this.service = service;
    }

    @PostMapping("v1/login")
    public ResponseEntity<BaseResponse> login(@Valid @RequestBody LoginUserRequest request){
        return service.login(request).apply();
    }

    @PostMapping("v1/create")
    public ResponseEntity<BaseResponse> create(@Valid @RequestBody CreateUserRequest request){
        return service.create(request).apply();
    }

}