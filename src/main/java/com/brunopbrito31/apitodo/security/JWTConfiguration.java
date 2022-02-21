package com.brunopbrito31.apitodo.security;

import com.brunopbrito31.apitodo.services.UserDatailServiceImp;

import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@EnableWebSecurity
public class JWTConfiguration extends WebSecurityConfigurerAdapter {

    private final UserDatailServiceImp usuarioService;
    private final PasswordEncoder passwordEncoder;

    public JWTConfiguration(UserDatailServiceImp usuarioService, PasswordEncoder passwordEncoder) {
        this.usuarioService = usuarioService;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception{
        auth.userDetailsService(usuarioService).passwordEncoder(passwordEncoder);
    }

    // Todas as Rotas estão permitidas, exceto a de tarefas e usuários
    @Override
    protected void configure(HttpSecurity http) throws Exception{
        http.csrf().disable().authorizeRequests() // habilitado para ambiente de producao
                .antMatchers(HttpMethod.POST, "/login").permitAll()
                .antMatchers("/api/tasks/**").authenticated()
                .antMatchers(HttpMethod.POST,"/api/tasks/save").permitAll()
                .antMatchers("/api/users/**").authenticated()
                .anyRequest().permitAll()
                .and().headers().frameOptions().sameOrigin() // adicionado pro h2 functionar
                .and()
                .addFilter(new JWTAutenticationFilter(authenticationManager()))
                .addFilter(new JWTValidateFilter(authenticationManager()))
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
    }

    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        final UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration corsConfiguration = new CorsConfiguration().applyPermitDefaultValues();
        source.registerCorsConfiguration("/**",corsConfiguration);
        return source;
    }

}
