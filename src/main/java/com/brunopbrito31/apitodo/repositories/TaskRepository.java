package com.brunopbrito31.apitodo.repositories;

import java.util.List;
import java.util.stream.Collectors;

import com.brunopbrito31.apitodo.models.entities.Task;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface TaskRepository  extends JpaRepository<Task, Long>{

    // Query que busca todas as tarefas de um usuário especifico pelo seu login
    @Query(
        value = "select "
                +"tb_tarefas.id, "
                +"tb_tarefas.descricao, "
                +"tb_tarefas.titulo, "
                +"tb_tarefas.id_usuario, "
                +"tb_tarefas.nivel_prioridade, "
                +"tb_tarefas.status "
                +"from tb_tarefas "
                +"left join tb_usuarios "
                +"on tb_tarefas.id_usuario = tb_usuarios.id "
                +"where tb_usuarios.email = :login ",
        nativeQuery = true
    )
    List<Task> findByResponsible( @Param("login")String login );

    // Query que busca todas as tarefas ativas (Abertas ou em progresso) filtrando pela prioridade
    @Query(
        value = "select "
                +"tb_tarefas.id, "
                +"tb_tarefas.descricao, "
                +"tb_tarefas.titulo, "
                +"tb_tarefas.id_usuario, "
                +"tb_tarefas.nivel_prioridade, "
                +"tb_tarefas.status "
                +"from tb_tarefas "
                +"left join tb_usuarios "
                +"on tb_tarefas.id_usuario = tb_usuarios.id "
                +"where tb_usuarios.email = :login "
                +"and tb_tarefas.nivel_prioridade = :priority "
                +"and tb_tarefas.status = 1 "
                +"or tb_tarefas.status = 0 ",
        nativeQuery = true
    )
    List<Task> findByPriority( @Param("login")String login, @Param("priority")Long priority );

    // Query para buscar todas as tarefas ativas
    @Query(
        value = "select "
                +"tb_tarefas.id, "
                +"tb_tarefas.descricao, "
                +"tb_tarefas.titulo, "
                +"tb_tarefas.id_usuario, "
                +"tb_tarefas.nivel_prioridade, "
                +"tb_tarefas.status "
                +"from tb_tarefas "
                +"left join tb_usuarios "
                +"on tb_tarefas.id_usuario = tb_usuarios.id "
                +"where tb_usuarios.email = :login "
                +"and tb_tarefas.status = 1 "
                +"or tb_tarefas.status = 0 "
                +"order by tb_tarefas.nivel_prioridade ",
        nativeQuery = true
    )
    List<Task> findAllActive(@Param("login") String login );

    // Abordagem alternativa, Busca de tarefas pela prioridade, optei pela query especifica para obter ganho de perfomance /
    default List<Task> findByPriorityAlternative( String login, Long priority ){
        List<Task> tasks = findAllActive( login );
        tasks = tasks.stream().filter( x -> x.getPriority().ordinal() == priority ).collect(Collectors.toList());
        return tasks;
    }

    // Query que busca todas as tarefas de um usuário especifico pelo seu login (Com Paginação)
    @Query(
            value = "select "
                    +"tb_tarefas.id, "
                    +"tb_tarefas.descricao, "
                    +"tb_tarefas.titulo, "
                    +"tb_tarefas.id_usuario, "
                    +"tb_tarefas.nivel_prioridade, "
                    +"tb_tarefas.status "
                    +"from tb_tarefas "
                    +"left join tb_usuarios "
                    +"on tb_tarefas.id_usuario = tb_usuarios.id "
                    +"where tb_usuarios.email = :login LIMIT :start, :size ",
            nativeQuery = true
    )
    List<Task> findByResponsibleWithPagination(
            @Param("login") String login ,
            @Param("start")Integer startLimit,
            @Param("size") Integer pageSize
    );


    // Query que conta a quantidade de  todas as tarefas de um usuário especifico pelo seu login
    @Query(
            value = "select "
                    +"COUNT(tb_tarefas.id) "
                    +"from tb_tarefas "
                    +"left join tb_usuarios "
                    +"on tb_tarefas.id_usuario = tb_usuarios.id "
                    +"where tb_usuarios.email = :login ",
            nativeQuery = true
    )
    Long findByResponsibleCount( @Param("login")String login );

    

}
