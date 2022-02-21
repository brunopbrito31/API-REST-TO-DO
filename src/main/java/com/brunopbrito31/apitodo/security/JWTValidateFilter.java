package com.brunopbrito31.apitodo.security;

import java.io.IOException;
import java.util.ArrayList;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;

import com.brunopbrito31.apitodo.controllers.TaskController;
import com.brunopbrito31.apitodo.controllers.UserModelController;
import com.brunopbrito31.apitodo.models.auxiliar.UserAut;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

public class JWTValidateFilter extends BasicAuthenticationFilter{

    private static final String HEADER_ATRIBUTO = "Authorization";
    private static final String ATRIBUTO_PREFIXO = "Bearer ";

    public JWTValidateFilter(AuthenticationManager authenticationManager) {
        super(authenticationManager);
        //TODO Auto-generated constructor stub
    }

    @Override
    protected void doFilterInternal(
        HttpServletRequest request,
        HttpServletResponse response,
        FilterChain chain
    )throws IOException, ServletException {
        
        String atributo = request.getHeader(HEADER_ATRIBUTO);

        if(atributo == null){
            chain.doFilter(request, response);
            return;
        }

        if(!atributo.startsWith(ATRIBUTO_PREFIXO)){
            chain.doFilter(request, response);
            return;

        }

        String token = atributo.replace(ATRIBUTO_PREFIXO,"");

        UsernamePasswordAuthenticationToken authenticationToken = getAuthenticationToken(token);

        SecurityContextHolder.getContext().setAuthentication(authenticationToken);
        
        // Seta o UserName - Login/Email na request
//        request.setAttribute("loginUser", authenticationToken.getPrincipal().toString() );

        UserAut.loginUser = authenticationToken.getPrincipal().toString();

        // Inserir o nome do usuário na request como atributo
        chain.doFilter(request, response);

    }


    private UsernamePasswordAuthenticationToken getAuthenticationToken(String token){
        String usuario = JWT.require(Algorithm.HMAC512(JWTAutenticationFilter.TOKEN_SENHA))
        .build()
        .verify(token)
        .getSubject();

        if(usuario == null){
            return null;
        }

        return new UsernamePasswordAuthenticationToken(usuario, null, new ArrayList<>());
    }
    
}
