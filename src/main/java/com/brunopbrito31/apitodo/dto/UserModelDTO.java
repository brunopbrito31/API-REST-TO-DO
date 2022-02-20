package com.brunopbrito31.apitodo.dto;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;


import lombok.Data;

@Data
public class UserModelDTO {

    private Long id;

    @NotBlank(message ="Campo não informado")
    @Pattern(regexp = "^[A-Z]+(.)*", message="O Nome deve conter a primeira letra maiuscula")
    private String name;

    @Email(message ="Email inválido")
    @NotBlank(message ="Campo não informado")
    private String login;

    @NotBlank(message ="Campo não informado")
    @Pattern(regexp = "[a-zA-Z]{3}[0-9]{5}",message="A Senha deve Conter 3 Letras seguidas de 5 números")
    private String password;
}
