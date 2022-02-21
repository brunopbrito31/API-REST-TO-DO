package com.brunopbrito31.apitodo.controllers;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.validation.Valid;

import com.brunopbrito31.apitodo.dto.UserModelDTO;
import com.brunopbrito31.apitodo.models.entities.UserModel;
import com.brunopbrito31.apitodo.repositories.UserModelRepository;

@RestController
@RequestMapping("/api/users")
@Api(value = "API REST Usuários")
@CrossOrigin(origins="*")
public class UserModelController {

    @Autowired
    private UserModelRepository usuarioModelRep;

    @Autowired
    private PasswordEncoder encoder;

    // Lista todos os usuários
    @GetMapping
    @ApiOperation(value="Retorna a lista de usuários")
    public ResponseEntity<List<UserModelDTO>> listAll(){
        return ResponseEntity.ok(usuarioModelRep.findAll().stream().map(user -> convertUserEntityToDTO(user)).collect(Collectors.toList()));
    }
    
    // Criação do usuário
    @PostMapping("/save")
    @ApiOperation(value="Cria um novo usuário")
    public ResponseEntity<UserModelDTO> create(@RequestBody @Valid UserModelDTO userModelDTO){
        UserModel userModel = convertUserDTOToEntity(userModelDTO);
        //encrypta a senha
        userModel.setPassword(encoder.encode(userModel.getPassword())); 
        userModel = usuarioModelRep.save(userModel);
        
        return ResponseEntity.ok().body(convertUserEntityToDTO(userModel));
    }

    // Verifica se a senha do usuário é válida
    @GetMapping("/passwordvalidate")
    @ApiOperation(value="Verifica se a Senha do usuário é válida")
    public ResponseEntity<Boolean> passwordValidate(@RequestParam String login, @RequestParam String password){
        
        Optional<UserModel> optUsuario = usuarioModelRep.findByLogin(login);
        if(!optUsuario.isPresent()){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(false);
        }
        
        UserModel usuario = optUsuario.get();

        // Verifica se a  senha que o usuario digitou e a mesma que esta no banco de dados
        // Compara a senha aberta com a senha criptografada
        boolean valid = encoder.matches(password,usuario.getPassword());

        HttpStatus status = (valid) ? HttpStatus.OK : HttpStatus.UNAUTHORIZED;

        return ResponseEntity.status(status).body(valid);
    }

    // Intercepta os erros de validação e retorna o erro de forma amigável para o usuário
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Map<String, String> handleValidationException(MethodArgumentNotValidException ex){
        Map<String, String> errors = new HashMap<>();

        ex.getBindingResult().getAllErrors().forEach((error)->{
            String fieldName = ((FieldError)error).getField();
            String errorMessage = error.getDefaultMessage();

            errors.put(fieldName, errorMessage);
        });

        return errors;
    }

    // Mapeamento Classe DTO x Entidade

    private UserModelDTO convertUserEntityToDTO(UserModel userModel){
        UserModelDTO userModelDTO = new UserModelDTO();
        userModelDTO.setId(userModel.getId());
        userModelDTO.setName(userModel.getName());
        userModelDTO.setLogin(userModel.getLogin());
        userModelDTO.setPassword("Confidencial");
        return userModelDTO;
    }

    private UserModel convertUserDTOToEntity(UserModelDTO userModelDTO){
        UserModel userModel = new UserModel();
        userModel.setId(userModelDTO.getId());
        userModel.setName(userModelDTO.getName());
        userModel.setLogin(userModelDTO.getLogin());
        userModel.setPassword(userModelDTO.getPassword());
        return userModel;
    }
    
    
}
