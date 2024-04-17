package ru.my.task.libraryreaders.security.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.my.task.libraryreaders.security.model.User;

public interface UserRepository extends JpaRepository<User, Long> {

    User findByUsername(String name);
}
