package com.brunopbrito31.apitodo.dto;

import com.brunopbrito31.apitodo.models.enums.TaskPriority;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Column;
import javax.validation.constraints.NotBlank;

@Data
public class TaskDTO {

    @ApiModelProperty(value = "Id da Tarefa")
    private Long id;

    @ApiModelProperty(value = "Título da Tarefa")
    @NotBlank(message ="Campo não informado")
    private String title;

    @ApiModelProperty(value = "Descrição da Tarefa")
    @NotBlank(message ="Campo não informado")
    private String description;

    @ApiModelProperty(value = "Prioridade da Tarefa, 0 = HIGHEST(Alta), 1 = MEDIUM(Média), 2 = LOWEST(Baixa)")
    @Column(name = "nivel_prioridade", nullable=false)
    private TaskPriority priority;
}
