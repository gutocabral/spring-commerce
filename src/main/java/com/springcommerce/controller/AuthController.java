package com.springcommerce.controller;

import com.springcommerce.dto.AuthRequestDTO;
import com.springcommerce.dto.AuthResponseDTO;
import com.springcommerce.dto.UserDTO;
import com.springcommerce.security.JwtTokenUtil;
import com.springcommerce.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final UserDetailsService userDetailsService;
    private final JwtTokenUtil jwtTokenUtil;
    private final UserService userService;

    @Autowired
    public AuthController(AuthenticationManager authenticationManager,
                          UserDetailsService userDetailsService,
                          JwtTokenUtil jwtTokenUtil,
                          UserService userService) {
        this.authenticationManager = authenticationManager;
        this.userDetailsService = userDetailsService;
        this.jwtTokenUtil = jwtTokenUtil;
        this.userService = userService;
    }

    @PostMapping("/login")
    public ResponseEntity<?> createAuthenticationToken(@Valid @RequestBody AuthRequestDTO authRequest) {
        try {
            // Autentica o usuário
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(authRequest.getEmail(), authRequest.getPassword())
            );
        } catch (BadCredentialsException e) {
            // Retorna erro se as credenciais forem inválidas
            return ResponseEntity.badRequest().body("Credenciais inválidas");
        }

        // Carrega os detalhes do usuário
        final UserDetails userDetails = userDetailsService.loadUserByUsername(authRequest.getEmail());

        // Gera o token JWT
        final String token = jwtTokenUtil.generateToken(userDetails);

        // Busca informações do usuário
        UserDTO user = userService.findByEmail(authRequest.getEmail());

        // Retorna o token e informações do usuário
        return ResponseEntity.ok(new AuthResponseDTO(token, user));
    }

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@Valid @RequestBody UserDTO userDTO) {
        try {
            // Salva o novo usuário
            UserDTO savedUser = userService.save(userDTO);
            return ResponseEntity.ok(savedUser);
        } catch (IllegalArgumentException e) {
            // Retorna erro se o email já estiver em uso
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
