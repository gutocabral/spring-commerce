package com.springcommerce.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CartItemDTO {
    private Long id; // ID do item no carrinho
    private Long productId; // ID do produto associado
    private Integer quantity; // Quantidade do produto
}
