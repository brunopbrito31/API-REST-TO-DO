package com.brunopbrito31.apitodo.models.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import com.brunopbrito31.apitodo.models.enums.TaskPriority;
import com.brunopbrito31.apitodo.models.enums.TaskStatus;
import com.fasterxml.jackson.annotation.JsonIgnore;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity(name="tb_tarefas")
public class Task {

    @ApiModelProperty(value = "Id da Tarefa")
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;

    @ApiModelProperty(value = "Título da Tarefa")
    @Column(name="titulo", nullable=false)
    private String title;

    @ApiModelProperty(value = "Descrição da Tarefa")
    @Column(name="descricao", nullable=false)
    private String description;

    @ApiModelProperty(value = "Prioridade da Tarefa, 0 = HIGHEST(Alta), 1 = MEDIUM(Média), 2 = LOWEST(Baixa)")
    @Column(name = "nivel_prioridade", nullable=false)
    private TaskPriority priority;

    @ApiModelProperty(value = "Status da Tarefa, 0 = OPEN, 1 = INPROGRESS, 2 = FINISH, 3 = CANCELED")
    @Column(name="status", nullable=false)
    private TaskStatus status;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "id_usuario",referencedColumnName = "id")
    private UserModel responsible;
    
}
