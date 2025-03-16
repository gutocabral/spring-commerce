package com.springcommerce.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductDTO {

    private Long id;

    @NotBlank(message = "Nome do produto é obrigatório")
    private String name;

    private String description;

    @NotNull(message = "Preço do produto é obrigatório")
    @Positive(message = "Preço deve ser maior que zero")
    private BigDecimal price;

    private String imageUrl;

    @NotNull(message = "Quantidade em estoque é obrigatória")
    @Positive(message = "Quantidade em estoque deve ser maior que zero")
    private Integer stockQuantity;
}