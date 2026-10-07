package com.cleanarch.infrastructure.web.controller;

import com.cleanarch.application.port.in.CreateUserCommand;
import com.cleanarch.application.port.in.CreateUserResult;
import com.cleanarch.application.usecase.CreateUserUseCase;
import com.cleanarch.application.usecase.GetAllUserUseCase;
import com.cleanarch.infrastructure.web.dto.CreateUserRequest;
import com.cleanarch.infrastructure.web.dto.UserResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * LAYER 3: INFRASTRUCTURE - Web Adapter
 * <p>
 * UserController - Handles HTTP requests and converts them to application commands
 */
@RestController
@RequestMapping("/api/users")
public class UserController {
    private final CreateUserUseCase createUserUseCase;
    private final GetAllUserUseCase getAllUserUseCase;

    public UserController(CreateUserUseCase createUserUseCase, GetAllUserUseCase getAllUserUseCase) {
        this.createUserUseCase = createUserUseCase;
        this.getAllUserUseCase = getAllUserUseCase;
    }

    @PostMapping
    public ResponseEntity<UserResponse> createUser(@RequestBody CreateUserRequest request) {
        // 1. Convert Web DTO to Application Command
        CreateUserCommand command = new CreateUserCommand(
                request.getEmail(),
                request.getFullName()
        );

        // 2. Execute use case
        CreateUserResult result = createUserUseCase.execute(command);

        // 3. Convert Result to Web Response
        UserResponse response = new UserResponse(
                result.getUserId(),
                result.getEmail(),
                result.getFullName(),
                result.getCreatedAt().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME),
                result.isActive() ? "ACTIVE" : "INACTIVE"
        );

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping
    public ResponseEntity<List<UserResponse>> getAllUser() {
        var users = getAllUserUseCase.execute();
        var response = users.stream().map(x ->
                new UserResponse(
                        x.getId().getValue(),
                        x.getEmail().getValue(),
                        x.getFullName(),
                        x.getCreatedAt().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME),
                        x.isActive() ? "ACTIVE" : "INACTIVE"
                )
        ).toList();
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
}
