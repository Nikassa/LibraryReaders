package ru.my.task.libraryreaders.service.auth;

import ru.my.task.libraryreaders.model.auth.User;

import java.util.List;

public interface UserService {

    User register(User user);

    List<User> getAll();

    User findByUsername(String username);

    User findById(Long id);

    void delete(Long id);
}
