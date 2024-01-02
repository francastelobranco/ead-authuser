package com.ead.authuser.controllers;

import com.ead.authuser.dtos.UserDto;
import com.ead.authuser.enums.UserStatus;
import com.ead.authuser.enums.UserType;
import com.ead.authuser.models.UserModel;
import com.ead.authuser.services.UserService;
import com.fasterxml.jackson.annotation.JsonView;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.ZoneId;

@Log4j2
@RestController
@CrossOrigin(origins = "*", maxAge = 3600)
@RequestMapping("/auth")
public class AuthenticationController {

    @Autowired
    UserService userService;

    @PostMapping("/signup")
    public ResponseEntity<Object> registerUser(@RequestBody @Validated(UserDto.UserView.RegistrationPost.class)
                                                   @JsonView(UserDto.UserView.RegistrationPost.class) UserDto userDto){
        log.debug("POST registerUser userDto recebido {} ", userDto.toString());
        if (userService.existsByUsername(userDto.getUsername())){
            log.warn("Erro: O usuário com nome {} já está sendo utilizado.", userDto.getUsername());
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Erro: O nome do usuário já está sendo utilizado.");
        }
        if (userService.existsByEmail(userDto.getEmail())){
            log.warn("Erro: O email {} já está sendo utilizado.", userDto.getEmail());
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Erro: O email já está sendo utilizado.");
        }

        UserModel userModel = new UserModel();

        BeanUtils.copyProperties(userDto, userModel);
        userModel.setUserStatus(UserStatus.ACTIVE);
        userModel.setUserType(UserType.STUDENT);
        userModel.setCreationDate(LocalDateTime.now(ZoneId.of("UTC")));
        userModel.setLastUpdateDate(LocalDateTime.now(ZoneId.of("UTC")));
        userService.save(userModel);
        log.debug("POST registerUser userModel salvo {} ", userModel.toString());
        log.debug("Usuário salvo com sucesso: {} ", userModel.getUserId());
        return ResponseEntity.status(HttpStatus.CREATED).body(userModel);
    }
}
