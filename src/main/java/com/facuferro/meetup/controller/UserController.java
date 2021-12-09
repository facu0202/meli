package com.facuferro.meetup.controller;

import com.facuferro.meetup.api.UserRequest;
import com.facuferro.meetup.api.UserResponse;
import com.facuferro.meetup.domain.User;
import com.facuferro.meetup.domain.UserState;
import com.facuferro.meetup.service.NotificationService;
import com.facuferro.meetup.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping(value = "users")
@Slf4j
@Api(description = "Endpoint para administrar usuarios.", tags = {"user"})
public class UserController {

    public UserController(UserService userService,
                          NotificationService notificationService
                            ) {
        this.userService = userService;
        this.notificationService = notificationService;
    }
    private UserService userService;
    private NotificationService notificationService;

    @ApiOperation(value = "Registro un usuario", notes = "Crea un usuario", tags = { "user" })
    @PostMapping
    public ResponseEntity<UserResponse> registerUser(@RequestBody @Valid UserRequest userRequest) {
        log.info("Register user for request {}",userRequest);
        User newUser = userService.create(userRequest, UserState.PENDING);
        notificationService.notifyNewUser(newUser);
        return ResponseEntity.ok(UserResponse.createFrom(newUser));
    }

    @ApiOperation(value = "Listado de usuarios", notes = "Lista los usuarios. Uso exclusivo para aministradores.", tags = { "user" })
    @GetMapping()
    public ResponseEntity<List<UserResponse>> findAll() {
        log.info("Find all User");
        List<UserResponse> responseList = userService.findAll().stream().map(UserResponse::createFrom).collect(Collectors.toList());
        return ResponseEntity.ok(responseList);
    }

    @ApiOperation(value = "Consulta de usuario", notes = "Muestra la informacion de un usuario. Uso exclusivo para aministradores.", tags = { "user" })
    @GetMapping(value = "/{id}")
    public ResponseEntity<UserResponse> find(@PathVariable("id") Long userId) {
        log.info("Find by id {}",userId);
        return ResponseEntity.ok(UserResponse.createFrom(userService.findById(userId)));
    }

    @ApiOperation(value = "Confirmacion de registro", notes = "Permite a un usuario confirmar su registro.", tags = { "user" })
    @GetMapping(value = "/{id}/confirm/{token}")
    @ResponseStatus(HttpStatus.OK)
    public void confirmRegistration(@PathVariable("id") Long userId, @PathVariable("token") String token) {
        log.info("Confirm user registrartion for userId {}",userId);
        User user = User.builder().id(userId).build();
        userService.confirm(user, token);
    }
}
