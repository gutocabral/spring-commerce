package com.springcommerce.service;

import com.springcommerce.dto.ProductDTO;
import com.springcommerce.model.Product;
import com.springcommerce.repository.ProductRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProductService {

    private final ProductRepository productRepository;

    @Autowired
    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Transactional(readOnly = true)
    public List<ProductDTO> findAll() {
        return productRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public ProductDTO findById(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Produto não encontrado com o ID: " + id));
        return convertToDTO(product);
    }

    @Transactional
    public ProductDTO save(ProductDTO productDTO) {
        Product product = convertToEntity(productDTO);
        Product savedProduct = productRepository.save(product);
        return convertToDTO(savedProduct);
    }

    @Transactional
    public ProductDTO update(Long id, ProductDTO productDTO) {
        Product existingProduct = productRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Produto não encontrado com o ID: " + id));

        // Mantém a data de criação e atualiza as outras propriedades
        Product updatedProduct = convertToEntity(productDTO);
        updatedProduct.setId(existingProduct.getId());
        updatedProduct.setCreatedAt(existingProduct.getCreatedAt());

        Product savedProduct = productRepository.save(updatedProduct);
        return convertToDTO(savedProduct);
    }

    @Transactional
    public void delete(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Produto não encontrado com o ID: " + id));
        productRepository.delete(product);
    }

    @Transactional(readOnly = true)
    public List<ProductDTO> findByNameContaining(String name) {
        return productRepository.findByNameContainingIgnoreCase(name).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<ProductDTO> findByStockGreaterThan(Integer minStock) {
        return productRepository.findByStockQuantityGreaterThan(minStock).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    // Métodos de conversão entre entidade e DTO
    private ProductDTO convertToDTO(Product product) {
        ProductDTO productDTO = new ProductDTO();
        BeanUtils.copyProperties(product, productDTO);
        return productDTO;
    }

    private Product convertToEntity(ProductDTO productDTO) {
        Product product = new Product();
        BeanUtils.copyProperties(productDTO, product);
        return product;
    }
}