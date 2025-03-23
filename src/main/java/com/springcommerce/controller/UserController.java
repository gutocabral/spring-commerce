package com.springcommerce.controller;

import com.springcommerce.dto.UserDTO;
import com.springcommerce.model.Role;
import com.springcommerce.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    // Endpoint para listar todos os usuários
    @GetMapping
    public ResponseEntity<List<UserDTO>> getAllUsers() {
        List<UserDTO> users = userService.findAll();
        return ResponseEntity.ok(users);
    }

    // Endpoint para buscar um usuário pelo ID
    @GetMapping("/{id}")
    public ResponseEntity<UserDTO> getUserById(@PathVariable Long id) {
        UserDTO user = userService.findById(id);
        return ResponseEntity.ok(user);
    }

    // Endpoint para criar um novo usuário
    @PostMapping
    public ResponseEntity<UserDTO> createUser(@Valid @RequestBody UserDTO userDTO) {
        UserDTO createdUser = userService.save(userDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdUser);
    }

    // Endpoint para atualizar um usuário existente
    @PutMapping("/{id}")
    public ResponseEntity<UserDTO> updateUser(
            @PathVariable Long id,
            @Valid @RequestBody UserDTO userDTO) {
        UserDTO updatedUser = userService.update(id, userDTO);
        return ResponseEntity.ok(updatedUser);
    }

    // Endpoint para remover um usuário
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        userService.delete(id);
        return ResponseEntity.noContent().build();
    }

    // Endpoint para adicionar um perfil a um usuário
    @PostMapping("/{id}/roles")
    public ResponseEntity<UserDTO> addRole(
            @PathVariable Long id,
            @RequestParam Role role) {
        UserDTO updatedUser = userService.addRole(id, role);
        return ResponseEntity.ok(updatedUser);
    }

    // Endpoint para remover um perfil de um usuário
    @DeleteMapping("/{id}/roles")
    public ResponseEntity<UserDTO> removeRole(
            @PathVariable Long id,
            @RequestParam Role role) {
        UserDTO updatedUser = userService.removeRole(id, role);
        return ResponseEntity.ok(updatedUser);
    }
}
