package ru.my.task.libraryreaders.repository.auth;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.my.task.libraryreaders.model.auth.Role;

public interface RoleRepository extends JpaRepository<Role, Long> {

    Role findByName(String name);
}
