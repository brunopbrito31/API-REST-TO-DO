package com.brunopbrito31.data;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Optional;

import com.brunopbrito31.apitodo.models.entities.UserModel;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

public class UserDetailsData implements UserDetails{

    private final Optional<UserModel> usuario;

    public UserDetailsData(Optional<UserModel> usuario){
        this.usuario = usuario;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return new ArrayList<>();
    }

    @Override
    public String getPassword() {
        return usuario.orElse(new UserModel()).getPassword();
    }

    @Override
    public String getUsername() {
        return usuario.orElse(new UserModel()).getLogin();
    }

    @Override
    public boolean isAccountNonExpired() {
        // TODO Auto-generated method stub
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        // TODO Auto-generated method stub
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        // TODO Auto-generated method stub
        return true;
    }

    @Override
    public boolean isEnabled() {
        // TODO Auto-generated method stub
        return true;
    }
    
}
