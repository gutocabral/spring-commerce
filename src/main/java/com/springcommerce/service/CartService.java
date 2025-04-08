package com.springcommerce.service;

import com.springcommerce.dto.CartDTO;
import com.springcommerce.dto.CartItemDTO;
import com.springcommerce.model.Cart;
import com.springcommerce.model.CartItem;
import com.springcommerce.model.Product;
import com.springcommerce.model.User;
import com.springcommerce.repository.CartRepository;
import com.springcommerce.repository.ProductRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.stream.Collectors;

@Service
public class CartService {

    private final CartRepository cartRepository;
    private final ProductRepository productRepository;

    @Autowired
    public CartService(CartRepository cartRepository, ProductRepository productRepository) {
        this.cartRepository = cartRepository;
        this.productRepository = productRepository;
    }

    // Adiciona um produto ao carrinho do usuário
    @Transactional
    public CartDTO addProductToCart(User user, Long productId, Integer quantity) {
        // Busca ou cria um novo carrinho para o usuário
        Cart cart = cartRepository.findByUser(user).orElseGet(() -> {
            Cart newCart = new Cart();
            newCart.setUser(user);
            return cartRepository.save(newCart);
        });

        // Verifica se o produto existe
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new EntityNotFoundException("Produto não encontrado"));

        // Verifica se o item já está no carrinho
        CartItem existingItem = cart.getItems().stream()
                .filter(item -> item.getProduct().getId().equals(productId))
                .findFirst()
                .orElse(null);

        if (existingItem != null) {
            // Atualiza a quantidade se já existir no carrinho
            existingItem.setQuantity(existingItem.getQuantity() + quantity);
        } else {
            // Adiciona novo item ao carrinho se não existir ainda
            CartItem newItem = new CartItem();
            newItem.setProduct(product);
            newItem.setQuantity(quantity);
            newItem.setCart(cart);
            cart.getItems().add(newItem);
        }

        // Recalcula o total e salva o carrinho atualizado
        cart.calculateTotal(); // Garante que o total seja calculado antes de salvar
        cartRepository.save(cart);

        return convertToDTO(cart);
    }

    // Obtém o carrinho do usuário autenticado
    @Transactional(readOnly = true)
    public CartDTO getCartByUser(User user) {
        Cart cart = cartRepository.findByUser(user)
                .orElseThrow(() -> new EntityNotFoundException("Carrinho não encontrado para o usuário"));
        return convertToDTO(cart);
    }

    // Método para converter um Cart em CartDTO
    private CartDTO convertToDTO(Cart cart) {
        CartDTO cartDTO = new CartDTO();
        cartDTO.setId(cart.getId());
        cartDTO.setUserId(cart.getUser().getId());
        cartDTO.setTotal(cart.getTotal());

        // Converte cada CartItem para CartItemDTO e adiciona à lista de itens
        cartDTO.setItems(cart.getItems().stream()
                .map(this::convertToCartItemDTO)
                .collect(Collectors.toList()));

        return cartDTO;
    }

    // Método para converter um CartItem em CartItemDTO
    private CartItemDTO convertToCartItemDTO(CartItem cartItem) {
        return new CartItemDTO(
                cartItem.getId(),
                cartItem.getProduct().getId(),
                cartItem.getQuantity()
        );
    }
}
