package com.brunopbrito31.apitodo.models.entities;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity(name="tb_usuarios")
public class UserModel {

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;

    @Column(name="nome", nullable=false)
    private String name;

    @Column(name="email",unique = true, nullable=false)
    private String login;

    // @JsonProperty(access = JsonProperty.Access.WRITE_ONLY) - Impede a exposição da senha 
    @Column(name="senha", nullable=false)
    private String password;

    @OneToMany(mappedBy = "responsible", fetch = FetchType.LAZY)
    private List<Task> usersTasks;

}
