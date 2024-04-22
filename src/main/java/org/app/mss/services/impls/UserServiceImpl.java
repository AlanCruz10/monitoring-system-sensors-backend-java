package org.app.mss.services.impls;

import org.app.mss.persistences.entities.User;
import org.app.mss.persistences.repositories.IUserRepository;
import org.app.mss.services.interfaces.IUserService;
import org.app.mss.web.dtos.requests.CreateUserRequest;
import org.app.mss.web.dtos.requests.LoginUserRequest;
import org.app.mss.web.dtos.responses.BaseResponse;
import org.app.mss.web.dtos.responses.UserResponse;
import org.app.mss.web.exceptions.BadRequestException;
import org.app.mss.web.exceptions.NotFoundDataException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserServiceImpl implements IUserService {

    private final IUserRepository repository;

    public UserServiceImpl(IUserRepository repository) {
        this.repository = repository;
    }

    @Override
    public BaseResponse login(LoginUserRequest request) {
        User user = findUserByEmail(request.getEmail());
        if (user.getPassword().equals(request.getPassword())) {
            return BaseResponse.builder()
                    .data(from(user))
                    .detail("User logged")
                    .success(Boolean.TRUE)
                    .status(HttpStatus.OK.value())
                    .httpStatus(HttpStatus.OK).build();
        }
        throw new BadRequestException();
    }

    @Override
    public BaseResponse create(CreateUserRequest request) {
        Optional<User> user = repository.findByEmail(request.getEmail());
        if (user.isEmpty()){
            return BaseResponse.builder()
                    .data(from(from(request)))
                    .detail("User created")
                    .success(Boolean.TRUE)
                    .status(HttpStatus.OK.value())
                    .httpStatus(HttpStatus.OK).build();
        }
        throw new BadRequestException(request.getEmail());
    }

    private User findUserByEmail(String email) {
        return repository.findByEmail(email).orElseThrow(() -> new NotFoundDataException(email));
    }

    private UserResponse from(User user){
        return UserResponse.builder()
                .id(user.getId())
                .name(user.getName())
                .lastName(user.getLastName())
                .build();
    }

    private User from(CreateUserRequest request){
        User user = new User();
        user.setName(request.getName());
        user.setLastName(request.getLastName());
        user.setEmail(request.getEmail());
        user.setPassword(request.getPassword());
        return repository.save(user);
    }

}