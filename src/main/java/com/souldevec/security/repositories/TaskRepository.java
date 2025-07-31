package com.souldevec.security.repositories;

import com.souldevec.security.entities.Task;
import com.souldevec.security.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface TaskRepository extends JpaRepository<Task, Long> {
    List<Task> findByAsignadoAOrderByFechaCreacionDesc(User asignadoA);
    List<Task> findAllByOrderByFechaCreacionDesc();
}
