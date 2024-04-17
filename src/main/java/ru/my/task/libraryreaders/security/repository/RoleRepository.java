package ru.my.task.libraryreaders.security.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.my.task.libraryreaders.security.model.Role;

public interface RoleRepository extends JpaRepository<Role, Long> {

    Role findByName(String name);
}
