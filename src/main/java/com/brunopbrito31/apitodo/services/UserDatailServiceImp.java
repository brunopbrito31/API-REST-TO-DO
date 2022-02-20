package com.brunopbrito31.apitodo.services;

import java.util.Optional;

import com.brunopbrito31.apitodo.models.entities.UserModel;
import com.brunopbrito31.apitodo.repositories.UserModelRepository;
import com.brunopbrito31.apitodo.data.UserDetailsData;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

@Component
public class UserDatailServiceImp implements UserDetailsService{

    private final UserModelRepository repository;

    public UserDatailServiceImp(UserModelRepository repository){
        this.repository = repository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<UserModel> usuario = repository.findByLogin(username);

        if(!usuario.isPresent()) {
            throw new UsernameNotFoundException("Usuário ["+username+"] nao encontrado");
        }

        return new UserDetailsData(usuario);
    }
    
}
