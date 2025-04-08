package com.springcommerce.repository;

import com.springcommerce.model.Cart;
import com.springcommerce.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CartRepository extends JpaRepository<Cart, Long> {

    Optional<Cart> findByUser(User user); // Busca o carrinho pelo usuário associado
}
