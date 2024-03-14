package ru.my.task.libraryreaders.repository.auth;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.my.task.libraryreaders.model.auth.User;

public interface UserRepository extends JpaRepository<User, Long> {

    User findByUsername(String name);
}
