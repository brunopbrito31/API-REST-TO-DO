package com.brunopbrito31.apitodo.models.entities;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity(name="tb_usuarios")
public class UserModel {

    @ApiModelProperty(value = "Id do Usuário")
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;

    @ApiModelProperty(value = "Nome do Usuário")
    @Column(name="nome", nullable=false)
    private String name;

    @ApiModelProperty(value = "Email/Login do Usuário")
    @Column(name="email",unique = true, nullable=false)
    private String login;

    @ApiModelProperty(value = "Senha do Usuário")
    @Column(name="senha", nullable=false)
    private String password;

    @ApiModelProperty(value = "Tarefas do Usuário")
    @OneToMany(mappedBy = "responsible", fetch = FetchType.LAZY)
    private List<Task> usersTasks;

}
