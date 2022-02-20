package com.brunopbrito31.apitodo.services;

import java.util.List;
import java.util.Optional;

import com.brunopbrito31.apitodo.models.entities.Task;
import com.brunopbrito31.apitodo.models.entities.UserModel;
import com.brunopbrito31.apitodo.models.enums.TaskStatus;
import com.brunopbrito31.apitodo.models.exceptions.BadRequestException;
import com.brunopbrito31.apitodo.repositories.TaskRepository;
import com.brunopbrito31.apitodo.repositories.UserModelRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TaskService {

    @Autowired
    private UserModelRepository userModelRepository;

    @Autowired
    private TaskRepository taskRepository;

    // Retorna todas as tarefas atreladas ao usuário
    public List<Task> allTasksByUser(String login){

        List<Task> taskSearched = taskRepository.findByResponsible(login);

        return taskSearched;
    }

    // Retorna todas as tarefas ativas atreladas ao usuário, já ordenadas pelo nível de prioridade
    public List<Task> searchActiveTasks(String login){
        return taskRepository.findAllActive(login);
    }

    // Retorna todas as tarefas ativas atreladas ao usuário com o filtro de prioridade
    public List<Task> searchActiveTasksByPriority(String login, Long priority){
        return taskRepository.findByPriority(login, priority);
    }

    // Retorna tarefa especifica pelo ID
    public Optional<Task> searchTaskByIdTask(String login, Long id){
        Optional<Task> taskSeached = taskRepository.findById(id);

        // Se a tarefa estiver associada a outro usuário limpa a variável de retorno
        if(taskSeached.isPresent() && !taskSeached.get().getResponsible().getLogin().equals(login)){
            taskSeached = null;
        }

        return taskSeached;
    }

    // Retorna as tarefas do usuário filtradas pelo nível de prioridade
    public List<Task> searchTasksByPriority(String login, Long priority){
        List<Task> tasksSearched = taskRepository.findByPriority( login, priority );
        return tasksSearched;
    }

    // Criação de tarefa com o login do usuário e a tarefa em si
    public Task createTask(String login, Task task){
        Optional<UserModel> userSearched = userByLoginValided(login);

        // Define o usuário atual como o responsável pela tarefa
        task.setResponsible(userSearched.get());

        return taskRepository.save(task);
    }

    // Atualiza uma tarefa
    public Task updateTask(String login, Task task){
        Optional<Task> taskSearched = taskByIdValided(task.getId());
        
        Task taskTemp = taskSearched.get();

        // Programação defensiva na verificação de autenticidade do usuário
        if(!taskTemp.getResponsible().getLogin().equals(login)){
            throw new BadRequestException("tarefa não encontrada");
        }

        if(task.getTitle() != null){
            taskTemp.setTitle(task.getTitle());
        }

        if(task.getDescription() != null){
            taskTemp.setDescription(task.getDescription());
        }

        if(task.getPriority() != null){
            taskTemp.setPriority(task.getPriority());
        }

        return taskRepository.save(taskTemp);
    }

    // Finaliza uma tarefa
    public Task finishTask(String login, Long idTask){
        Optional<Task> taskSearched = taskByIdValided(idTask);
        
        Task taskTemp = taskSearched.get();

        // Programação defensiva na verificação de autenticidade do usuário
        if(!taskTemp.getResponsible().getLogin().equals(login)){
            throw new BadRequestException("tarefa não encontrada");
        }
        TaskStatus status = taskTemp.getStatus();

        // Verifica a tarefa está apta para a conclusão
        if(status == TaskStatus.OPEN || status == TaskStatus.INPROGRESS){
            taskTemp.setStatus(TaskStatus.FINISH);
            taskRepository.save(taskTemp);

        }else{
            throw new BadRequestException("Só é possível concluir uma tarefa iniciada ou em progresso");
        }

        return taskRepository.save(taskTemp);
    }

    // Deleta uma tarefa associada ao usuário
    public void deleteTask(String login, Long idTask) {
        Optional<Task> taskSearched = taskByIdValided(idTask);

        // Verifica se o usuário presente no token é o responsável pela tarefa, em caso positivo deleta a tarefa
        if(taskSearched.get().getResponsible().getLogin().equals(login)){
            taskRepository.deleteById(idTask);

        }else{
            throw new BadRequestException("Tarefa não encontrada");
        }
    }

    // Cancela uma tarefa
    public void cancelTask(String login, Long idTask){
        Optional<Task> taskSearched = taskByIdValided(idTask);

        // Verifica se o usuário presente no token é o responsável pela tarefa, em caso positivo cancela a tarefa
        if(taskSearched.get().getResponsible().getLogin().equals(login)){
            Task taskTemp = taskSearched.get();
            TaskStatus status = taskTemp.getStatus();

            // Verifica a tarefa está apta para o cancelamento
            if(status == TaskStatus.OPEN || status == TaskStatus.INPROGRESS){
                taskTemp.setStatus(TaskStatus.CANCELED);
                taskRepository.save(taskTemp);

            }else{
                throw new BadRequestException("Só é possível cancelar uma tarefa iniciada ou em progresso");
            }

        // Caso a tarefa esteja associada a outro usuário
        }else{
            throw new BadRequestException("Tarefa não Encontrada!");
        }
    }

    // Busca o usuário no banco e caso não exista lança uma exceção
    private Optional<UserModel> userByLoginValided(String login){
        Optional<UserModel> userSearched = userModelRepository.findByLogin(login);

        if(!userSearched.isPresent()) throw new IllegalArgumentException("Usuário não encontrado!");

        return userSearched;
    }

    // Busca a tarefa e caso não encontre lança uma exceção
    private Optional<Task> taskByIdValided(Long idTask){
         Optional<Task> taskSearched = taskRepository.findById(idTask);

        if(!taskSearched.isPresent()) throw new IllegalArgumentException("Tarefa não encontrada!");

        return taskSearched;
    }


}
