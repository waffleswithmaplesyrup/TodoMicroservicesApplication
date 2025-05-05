package com.waffles.todo_microservice.TodoService.Model.request;

import lombok.Data;

@Data
public class NewTodoRequest {

    private String content;
}
