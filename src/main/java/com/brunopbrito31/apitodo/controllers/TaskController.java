package com.brunopbrito31.apitodo.controllers;

import java.net.URI;
import java.util.List;
import java.util.Optional;

import com.brunopbrito31.apitodo.models.entities.Task;
import com.brunopbrito31.apitodo.models.exceptions.BadRequestException;
import com.brunopbrito31.apitodo.services.TaskService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@RestController
@RequestMapping("/api/tasks")
public class TaskController {
    
    @Autowired
    private TaskService taskService;

    // Listar todas as tarefas do usuário
    @GetMapping
    public ResponseEntity<List<Task>> getTasks(
        @RequestAttribute String loginUser
    ){
        List<Task> tasks = taskService.allTasksByUser(loginUser);
        
        return tasks.isEmpty() ? 
            ResponseEntity.noContent().build() 
            : ResponseEntity.ok().body(tasks);
    }

    // Listar todas as tarefas ativas do usuário com ou sem filtro de prioridade
    @GetMapping("/active")
    public ResponseEntity<List<Task>> getActiveTasks(
        @RequestAttribute String loginUser,
        @RequestParam(defaultValue = "-1") Long priority // Valor aleatório mockado para controle
    ){
        List<Task> tasks;
        if(priority == -1){
            tasks = taskService.searchActiveTasks(loginUser);
        
        // Filtro de Prioridade Ativo
        }else{
            tasks = taskService.searchActiveTasksByPriority(loginUser, priority);
        }
        
        return tasks.isEmpty() ? 
            ResponseEntity.noContent().build() 
            : ResponseEntity.ok().body(tasks);
    }

    // Busca uma tarefa especifica pelo Id dela, com verificação de usuário
    @GetMapping("/{id}")
    public ResponseEntity<Task> getTaskById(
        @PathVariable Long id,
        @RequestAttribute String loginUser
    ){
        Optional<Task> taskSearched = taskService.searchTaskByIdTask(loginUser, id);
        
        return taskSearched.isPresent() ? 
            ResponseEntity.ok().body(taskSearched.get()) 
            : ResponseEntity.notFound().build();
    }

    // Criar uma nova tarefa - Falta implementar as validações
    @PostMapping
    public ResponseEntity<Task> createTask(
        @RequestAttribute String loginUser,
        @RequestBody Task task
    ){
        try {
            Task taskCreated = taskService.createTask(loginUser, task);
    
            // Monta a Url da nova tarefa
            URI uri = ServletUriComponentsBuilder.fromCurrentRequestUri().path("/{id}")
                .buildAndExpand(taskCreated.getId()).toUri();
    
            return ResponseEntity.created(uri).body(taskCreated);

        }catch(RuntimeException e){
            return ResponseEntity.internalServerError().build();
        }
    }

    // Cancela uma tarefa atrelada ao usuário
    @PutMapping("/cancel")
    public ResponseEntity<?> cancellTask(
        @RequestAttribute String loginUser,
        @RequestParam Long idTask
    ){
        try{
            taskService.cancelTask(loginUser, idTask);
            return ResponseEntity.ok().build();

        }catch(BadRequestException el){
            return ResponseEntity.badRequest().body(el.getMessage());
        }catch(RuntimeException e){
            return ResponseEntity.internalServerError().build();
        }
    }

    // Atualiza uma tarefa atrelada ao usuário
    @PutMapping("/update")
    public ResponseEntity<?> updateTask(
        @RequestAttribute String loginUser,
        @RequestBody Task task
    ){
        try{
            task =  taskService.updateTask(loginUser, task);
            return ResponseEntity.ok().body(task);

        }catch(BadRequestException el){
            return ResponseEntity.badRequest().body(el.getMessage());

        }catch(RuntimeException e){
            return ResponseEntity.internalServerError().build();
        }
    }

    // Finaliza uma tarefa iniciada ou em progresso
    @PutMapping("/finish")
    public ResponseEntity<String> finishTask(
        @RequestAttribute String loginUser,
        @RequestParam Long idTask
    ){
        try{
            taskService.finishTask(loginUser, idTask);
            return ResponseEntity.ok().build();

        }catch(BadRequestException el){
            return ResponseEntity.badRequest().body(el.getMessage());

        }catch(RuntimeException e){
            return ResponseEntity.internalServerError().build();
        }
    }

    // Remove uma tarefa atrelada ao usuário
    @DeleteMapping("/delete")
    public ResponseEntity<String> deleteTask(
        @RequestAttribute String loginUser,
        @RequestParam Long idTask
    ){
        try{
            taskService.deleteTask(loginUser, idTask);    
            return ResponseEntity.noContent().build();

        }catch(BadRequestException el){
            return ResponseEntity.badRequest().body(el.getMessage());

        }catch(RuntimeException e){
            return ResponseEntity.internalServerError().build();
        }

    }

}
