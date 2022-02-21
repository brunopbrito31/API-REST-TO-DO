package com.brunopbrito31.apitodo.dto;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class UserModelDTO {

    @ApiModelProperty(value = "Id do Usuário")
    private Long id;

    @ApiModelProperty(value = "Nome do Usuário")
    @NotBlank(message ="Campo não informado")
    @Pattern(regexp = "^[A-Z]+(.)*", message="O Nome deve conter a primeira letra maiuscula")
    private String name;

    @ApiModelProperty(value = "Email/Login do Usuário")
    @Email(message ="Email inválido")
    @NotBlank(message ="Campo não informado")
    private String login;

    @ApiModelProperty(value = "Senha do Usuário")
    @NotBlank(message ="Campo não informado")
    @Pattern(regexp = "[a-zA-Z]{3}[0-9]{5}",message="Sua Senha deve Conter 8 digitos, sendo 3 Letras seguidas de 5 números")
    private String password;
}
