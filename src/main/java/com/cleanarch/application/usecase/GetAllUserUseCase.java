package com.cleanarch.application.usecase;

import com.cleanarch.application.port.in.CreateUserResult;
import com.cleanarch.application.port.out.UserRepositoryPort;
import com.cleanarch.domain.model.User;

import java.util.List;

public class GetAllUserUseCase {
    private final UserRepositoryPort userRepository;

    public GetAllUserUseCase(UserRepositoryPort userRepository) {
        this.userRepository = userRepository;
    }

    public List<User> execute() {
        return  userRepository.getAll();
    }
}
