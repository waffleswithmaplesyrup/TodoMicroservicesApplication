package com.waffles.todo_microservice.TodoService.Service;

import com.waffles.todo_microservice.Feign.AuthMicroservice;
import com.waffles.todo_microservice.Security.TokenService;
import com.waffles.todo_microservice.StandardResponse.RestResponse;
import com.waffles.todo_microservice.TodoService.Model.Todo;
import com.waffles.todo_microservice.TodoService.Model.request.NewTodoRequest;
import com.waffles.todo_microservice.TodoService.Repository.TodoRepository;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class TodoService {

    private final TodoRepository todoRepository;
    private final TokenService tokenService;
    private final AuthMicroservice authMicroservice;

    @Autowired
    public TodoService(
            TodoRepository todoRepository,
            TokenService tokenService,
            AuthMicroservice authMicroservice
    ) {
        this.todoRepository = todoRepository;
        this.tokenService = tokenService;
        this.authMicroservice = authMicroservice;
    }

    public Todo createNewTodo(NewTodoRequest newTodo, HttpServletRequest request) {
        if(newTodo.getContent() == null || newTodo.getContent().isEmpty()) throw new RuntimeException("Empty content");

        // retrieve the user who made the todo from HttpServletRequest
        UUID userId = UUID.fromString(retrieveUserId(request));

        Todo todo = new Todo();
        todo.setContent(newTodo.getContent());
        todo.setStatus("Pending");
        todo.setUserId(userId);

        return todoRepository.save(todo);
    }

    public List<Todo> viewAllTodo(HttpServletRequest request) {

        UUID userId = UUID.fromString(retrieveUserId(request));

        return todoRepository.findAllByUserId(userId);
    }

    private String retrieveUserId(HttpServletRequest request) {

        // TODO: retrieve token, pass token to AUTH-MICROSERVICE
        // TODO: AUTH-MICROSERVICE will find the logged in user and return userId
        String token = tokenService.retrieveToken(request);

        try {
            ResponseEntity<RestResponse> response = authMicroservice.validateToken(token);

            if(response.getBody() == null || response.getBody().getData() == null) throw new RuntimeException("Unable to retrieve user");

            return response.getBody().getData().toString();
        } catch (RuntimeException e) {
            return e.getMessage();
        }
    }

}
