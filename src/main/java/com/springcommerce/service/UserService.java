package com.springcommerce.service;

import com.springcommerce.dto.UserDTO;
import com.springcommerce.model.Role;
import com.springcommerce.model.User;
import com.springcommerce.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    // Busca todos os usuários
    @Transactional(readOnly = true)
    public List<UserDTO> findAll() {
        return userRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    // Busca um usuário pelo ID
    @Transactional(readOnly = true)
    public UserDTO findById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Usuário não encontrado com o ID: " + id));
        return convertToDTO(user);
    }

    // Busca um usuário pelo email
    @Transactional(readOnly = true)
    public UserDTO findByEmail(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new EntityNotFoundException("Usuário não encontrado com o email: " + email));
        return convertToDTO(user);
    }

    // Salva um novo usuário
    @Transactional
    public UserDTO save(UserDTO userDTO) {
        // Verifica se já existe um usuário com o mesmo email
        if (userRepository.existsByEmail(userDTO.getEmail())) {
            throw new IllegalArgumentException("Email já está em uso: " + userDTO.getEmail());
        }

        // Converte DTO para entidade
        User user = convertToEntity(userDTO);

        // Criptografa a senha antes de salvar
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        // Salva o usuário no banco de dados
        User savedUser = userRepository.save(user);

        return convertToDTO(savedUser);
    }

    // Atualiza um usuário existente
    @Transactional
    public UserDTO update(Long id, UserDTO userDTO) {
        User existingUser = userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Usuário não encontrado com o ID: " + id));

        // Verifica se o email já está em uso por outro usuário
        if (!existingUser.getEmail().equals(userDTO.getEmail()) &&
                userRepository.existsByEmail(userDTO.getEmail())) {
            throw new IllegalArgumentException("Email já está em uso: " + userDTO.getEmail());
        }

        // Mantém a data de criação e atualiza as outras propriedades
        User updatedUser = convertToEntity(userDTO);
        updatedUser.setId(existingUser.getId());
        updatedUser.setCreatedAt(existingUser.getCreatedAt());

        // Se a senha for vazia, mantém a senha atual
        if (userDTO.getPassword() == null || userDTO.getPassword().isEmpty()) {
            updatedUser.setPassword(existingUser.getPassword());
        } else {
            // Caso contrário, criptografa a nova senha
            updatedUser.setPassword(passwordEncoder.encode(userDTO.getPassword()));
        }

        User savedUser = userRepository.save(updatedUser);
        return convertToDTO(savedUser);
    }

    // Remove um usuário
    @Transactional
    public void delete(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Usuário não encontrado com o ID: " + id));
        userRepository.delete(user);
    }

    // Adiciona um perfil ao usuário
    @Transactional
    public UserDTO addRole(Long userId, Role role) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("Usuário não encontrado com o ID: " + userId));
        user.getRoles().add(role);
        User savedUser = userRepository.save(user);
        return convertToDTO(savedUser);
    }

    // Remove um perfil do usuário
    @Transactional
    public UserDTO removeRole(Long userId, Role role) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("Usuário não encontrado com o ID: " + userId));
        user.getRoles().remove(role);
        User savedUser = userRepository.save(user);
        return convertToDTO(savedUser);
    }

    // Métodos de conversão entre entidade e DTO
    private UserDTO convertToDTO(User user) {
        UserDTO userDTO = new UserDTO();
        BeanUtils.copyProperties(user, userDTO);
        // Não incluímos a senha no DTO por segurança
        userDTO.setPassword(null);
        return userDTO;
    }

    private User convertToEntity(UserDTO userDTO) {
        User user = new User();
        BeanUtils.copyProperties(userDTO, user);
        return user;
    }
}
