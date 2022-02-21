package com.brunopbrito31.apitodo.controllers;

import java.net.URI;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import com.brunopbrito31.apitodo.dto.TaskDTO;
import com.brunopbrito31.apitodo.models.auxiliar.UserAut;
import com.brunopbrito31.apitodo.models.entities.Task;
import com.brunopbrito31.apitodo.models.enums.TaskStatus;
import com.brunopbrito31.apitodo.models.exceptions.BadRequestException;
import com.brunopbrito31.apitodo.services.TaskService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/tasks")
@Api(value = "API REST Tarefas")
public class TaskController {

    @Autowired
    private TaskService taskService;

    // Listar todas as tarefas do usuário
    @GetMapping
    @ApiOperation(value = "Lista todas as tarefas do usuário")
    public ResponseEntity<List<TaskDTO>> getTasks() {
        System.out.println("Entrou nas tarefas, valor de login = " + UserAut.loginUser);
        List<Task> tasks = taskService.allTasksByUser(UserAut.loginUser);
        //Converter as tarefas para DTO no retorno

        return tasks.isEmpty() ?
                ResponseEntity.noContent().build()
                : ResponseEntity.ok().body(
                tasks.stream().map(x -> convertTaskEntityToDTO(x)).collect(Collectors.toList()) // Converte todas tarefas presentes para DTO
        );
    }

    // Listar todas as tarefas do usuário (Com Paginação)
    @GetMapping("/with-pagination")
    @ApiOperation(value = "Lista todas as tarefas do usuário")
    public ResponseEntity<List<TaskDTO>> getTasksWithPagination(
            @RequestParam(defaultValue = "0") Integer pageNo,
            @RequestParam(defaultValue = "10") Integer pageSize
    ) {
        List<Task> tasks = taskService.allTasksByUserWithPagination(
                UserAut.loginUser,
                pageNo,
                pageSize
        );

        return tasks.isEmpty() ?
                ResponseEntity.noContent().build()
                : ResponseEntity.ok().body(
                tasks.stream().map(x -> convertTaskEntityToDTO(x)).collect(Collectors.toList()) // Converte todas tarefas presentes para DTO
        );
    }

    // Listar todas as tarefas ativas do usuário com ou sem filtro de prioridade
    @GetMapping("/active")
    @ApiOperation(value = "Lista todas as tarefas ativas do usuário com ou sem filtro de prioridade")
    public ResponseEntity<List<TaskDTO>> getActiveTasks(
            @RequestParam(defaultValue = "-1") Long priority // Valor aleatório mockado para controle
    ) {
        List<Task> tasks;
        if (priority == -1) {
            tasks = taskService.searchActiveTasks(UserAut.loginUser);

            // Filtro de Prioridade Ativo
        } else {
            tasks = taskService.searchActiveTasksByPriority(UserAut.loginUser, priority);
        }

        return tasks.isEmpty() ?
                ResponseEntity.noContent().build()
                : ResponseEntity.ok().body(
                tasks.stream().map(x -> convertTaskEntityToDTO(x))
                        .collect(Collectors.toList()) // Converte todas tarefas presentes para DTO
        );
    }

    // Busca uma tarefa especifica pelo Id dela, com verificação de usuário
    @GetMapping("/{id}")
    @ApiOperation(value = "Busca uma tarefa especifica do usuário, pelo id dela")
    public ResponseEntity<TaskDTO> getTaskById(
            @PathVariable Long id
    ) {
        Optional<Task> taskSearched = taskService.searchTaskByIdTask(UserAut.loginUser, id);

        return taskSearched.isPresent() ?
                ResponseEntity.ok().body(convertTaskEntityToDTO(taskSearched.get()))
                : ResponseEntity.notFound().build();
    }

    // Criar uma nova tarefa
    @PostMapping
    @ApiOperation(value = "Cria uma nova tarefa")
    public ResponseEntity<TaskDTO> createTask(@RequestBody @Valid TaskDTO taskDTO) {
        try {
            Task task = convertTaskDTOToEntity(taskDTO);
            // Seta o status da tarefa como aberta
            task.setStatus(TaskStatus.OPEN);

            Task taskCreated = taskService.createTask(UserAut.loginUser, task);

            // Monta a Url da nova tarefa
            URI uri = ServletUriComponentsBuilder.fromCurrentRequestUri().path("/{id}")
                    .buildAndExpand(taskCreated.getId()).toUri();

            return ResponseEntity.created(uri).body(convertTaskEntityToDTO(taskCreated));

        } catch (RuntimeException e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    // Atualiza o status da tarefa do usuário para em andamento
    @PutMapping("/in-progress")
    @ApiOperation(value = "Atualiza uma tarefa com o status de aberta para em progresso")
    public ResponseEntity<?> makeTask(@RequestParam Long idTask) {
        try {
            taskService.makeTask(UserAut.loginUser, idTask);
            return ResponseEntity.ok().build();

        } catch (BadRequestException el) {
            return ResponseEntity.badRequest().body(el.getMessage());
        } catch (RuntimeException e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    // Cancela uma tarefa atrelada ao usuário
    @PutMapping("/cancel")
    @ApiOperation(value = "Cancela uma tarefa atrelada ao usuário pelo id dela")
    public ResponseEntity<?> cancellTask(@RequestParam Long idTask) {
        try {
            taskService.cancelTask( UserAut.loginUser, idTask);
            return ResponseEntity.ok().build();

        } catch (BadRequestException el) {
            return ResponseEntity.badRequest().body(el.getMessage());
        } catch (RuntimeException e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    // Atualiza uma tarefa atrelada ao usuário
    @PutMapping("/update")
    @ApiOperation(value = "Atualiza uma tarefa atrelada ao usuário")
    public ResponseEntity<?> updateTask(@RequestBody TaskDTO taskDTO) {
        try {
            Task task = convertTaskDTOToEntity(taskDTO);
            task = taskService.updateTask( UserAut.loginUser, task);
            return ResponseEntity.ok().body(convertTaskEntityToDTO(task));

        } catch (BadRequestException el) {
            return ResponseEntity.badRequest().body(el.getMessage());

        } catch (RuntimeException e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    // Finaliza uma tarefa iniciada ou em progresso
    @PutMapping("/finish")
    @ApiOperation(value = "Conclui uma tarefa iniciada ou em progresso")
    public ResponseEntity<String> finishTask(@RequestParam Long idTask) {
        try {
            taskService.finishTask( UserAut.loginUser, idTask);
            return ResponseEntity.ok().build();

        } catch (BadRequestException el) {
            return ResponseEntity.badRequest().body(el.getMessage());

        } catch (RuntimeException e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    // Remove uma tarefa atrelada ao usuário
    @DeleteMapping("/delete")
    @ApiOperation(value = "Remove uma tarefa atrelada ao usuário")
    public ResponseEntity<String> deleteTask(@RequestParam Long idTask) {
        try {
            taskService.deleteTask( UserAut.loginUser, idTask);
            return ResponseEntity.noContent().build();

        } catch (BadRequestException el) {
            return ResponseEntity.badRequest().body(el.getMessage());

        } catch (RuntimeException e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    // Mapeamento Entidade X DTO
    private TaskDTO convertTaskEntityToDTO(Task task) {
        TaskDTO taskDTO = new TaskDTO();
        taskDTO.setId(task.getId());
        taskDTO.setTitle(task.getTitle());
        taskDTO.setDescription(task.getDescription());
        taskDTO.setPriority(task.getPriority());
        return taskDTO;
    }

    private Task convertTaskDTOToEntity(TaskDTO taskDTO) {
        Task task = new Task();
        task.setId(taskDTO.getId());
        task.setTitle(taskDTO.getTitle());
        task.setDescription(taskDTO.getDescription());
        task.setPriority(taskDTO.getPriority());
        return task;
    }

}
