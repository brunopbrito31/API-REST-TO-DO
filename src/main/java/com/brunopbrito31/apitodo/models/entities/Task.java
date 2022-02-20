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

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity(name="tb_tarefas")
public class Task {

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;
    
    @Column(name="titulo", nullable=false)
    private String title;

    @Column(name="descricao", nullable=false)
    private String description;

    @Column(name = "nivel_prioridade", nullable=false)
    private TaskPriority priority;

    @Column(name="status", nullable=false)
    private TaskStatus status;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "id_usuario",referencedColumnName = "id")
    private UserModel responsible;
    
}
