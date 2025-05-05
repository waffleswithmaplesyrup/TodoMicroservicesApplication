package com.waffles.todo_microservice.TodoService.Repository;

import com.waffles.todo_microservice.TodoService.Model.Todo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface TodoRepository extends JpaRepository<Todo, Long> {
    List<Todo> findAllByUserId(UUID userId);
}
