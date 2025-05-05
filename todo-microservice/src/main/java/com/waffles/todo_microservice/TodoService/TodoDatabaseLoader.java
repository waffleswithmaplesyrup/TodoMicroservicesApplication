package com.waffles.todo_microservice.TodoService;

import com.waffles.todo_microservice.TodoService.Service.TodoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Service;

@Service
public class TodoDatabaseLoader implements ApplicationRunner {

    private TodoService todoService;

    @Autowired
    public TodoDatabaseLoader(
            TodoService todoService
    ) {
        this.todoService = todoService;
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
//
//        NewTodoRequest newTodo = new NewTodoRequest();
//        newTodo.setContent("Do leetcode questions");
//
//        todoService.createNewTodo(newTodo);
//
//        newTodo.setContent("Clean cat litter");
//
//        todoService.createNewTodo(newTodo);

    }
}
