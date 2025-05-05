package com.waffles.todo_microservice.TodoService.Controller;

import com.waffles.todo_microservice.StandardResponse.RestResponse;
import com.waffles.todo_microservice.TodoService.Model.request.NewTodoRequest;
import com.waffles.todo_microservice.TodoService.Service.TodoService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/todo")
public class TodoController {

    private final TodoService todoService;

    @Autowired
    public TodoController(
            TodoService todoService
    ) {
        this.todoService = todoService;
    }

    ///  Only the user logged in can see their todo
    @GetMapping("/viewAll")
    public ResponseEntity<RestResponse> viewAllTodo(HttpServletRequest request) {
        try {
            return ResponseEntity.ok(
                    new RestResponse().readSuccess(
                            todoService.viewAllTodo(request)
                    )
            );
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(
                    new RestResponse().failure(e.getMessage())
            );
        }
    }

    @PostMapping("/create")
    public ResponseEntity<RestResponse> createNewTodo(
            @RequestBody NewTodoRequest newTodoRequest,
            HttpServletRequest request
    ) {
        try {
            return ResponseEntity.ok(
                    new RestResponse().readSuccess(
                            todoService.createNewTodo(newTodoRequest, request)
                    )
            );
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(
                    new RestResponse().failure(e.getMessage())
            );
        }
    }
}
