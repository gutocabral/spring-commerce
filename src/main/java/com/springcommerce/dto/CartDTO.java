package com.springcommerce.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CartDTO {
    private Long id; // ID do carrinho
    private Long userId; // ID do usuário associado ao carrinho
    private List<CartItemDTO> items; // Lista de itens no carrinho
    private Double total; // Total do valor do carrinho
}
