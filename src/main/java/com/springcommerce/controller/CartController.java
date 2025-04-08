package com.springcommerce.controller;

import com.springcommerce.dto.CartDTO;
import com.springcommerce.model.User;
import com.springcommerce.repository.UserRepository;
import com.springcommerce.service.CartService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/cart")
public class CartController {

    private final CartService cartService;
    private final UserRepository userRepository;

    @Autowired
    public CartController(CartService cartService, UserRepository userRepository) {
        this.cartService = cartService;
        this.userRepository = userRepository;
    }

    // Endpoint para adicionar um produto ao carrinho
    @PostMapping("/add")
    public ResponseEntity<CartDTO> addProductToCart(
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestParam Long productId,
            @RequestParam Integer quantity) {

        // Busca o usuário autenticado pelo email
        User user = userRepository.findByEmail(userDetails.getUsername())
                .orElseThrow(() -> new EntityNotFoundException("Usuário não encontrado"));

        // Adiciona o produto ao carrinho do usuário autenticado
        CartDTO updatedCart = cartService.addProductToCart(user, productId, quantity);
        return ResponseEntity.ok(updatedCart);
    }

    // Endpoint para visualizar o conteúdo do carrinho
    @GetMapping
    public ResponseEntity<CartDTO> getCart(@AuthenticationPrincipal UserDetails userDetails) {
        // Busca o usuário autenticado pelo email
        User user = userRepository.findByEmail(userDetails.getUsername())
                .orElseThrow(() -> new EntityNotFoundException("Usuário não encontrado"));

        // Busca o carrinho do usuário autenticado
        CartDTO cart = cartService.getCartByUser(user);
        return ResponseEntity.ok(cart);
    }
}
