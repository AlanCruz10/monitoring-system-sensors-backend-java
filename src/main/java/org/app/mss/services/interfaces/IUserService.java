package org.app.mss.services.interfaces;

import org.app.mss.web.dtos.requests.CreateUserRequest;
import org.app.mss.web.dtos.requests.LoginUserRequest;
import org.app.mss.web.dtos.responses.BaseResponse;

public interface IUserService {

    BaseResponse login(LoginUserRequest request);

    BaseResponse create(CreateUserRequest request);

}