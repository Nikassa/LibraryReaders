package ru.my.task.libraryreaders.security.service;

import ru.my.task.libraryreaders.security.model.User;

import java.util.List;

public interface UserService {

    User register(User user);

    List<User> getAll();

    User findByUsername(String username);

    User findById(Long id);

    void delete(Long id);
}
