package com.farmapp.backend.service;

import com.farmapp.backend.dto.ProductDto;
import com.farmapp.backend.model.Product;
import com.farmapp.backend.repository.ProductRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Service
public class ProductService {

    private final ProductRepository productRepository;

    // require ProductRepository bean (Spring Data JPA repository interface)
    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public List<ProductDto> listAll() {
        return productRepository.findAll().stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    public ProductDto getById(Long id) {
        return productRepository.findById(id)
                .map(this::toDto)
                .orElse(null);
    }

    public ProductDto createProduct(Long farmerId, ProductDto dto) {
        Product p = new Product();
        p.setFarmerId(farmerId);
        p.setName(dto.getName());
        p.setCategory(dto.getCategory());
        p.setUnit(dto.getUnit());
        p.setPricePerUnit(dto.getPricePerUnit());
        p.setQuantityAvailable(dto.getQuantityAvailable());
        p.setImageUrl(dto.getImageUrl());

        Product saved = productRepository.save(p);
        return toDto(saved);
    }

    public List<ProductDto> listByFarmer(Long farmerId) {
        return productRepository.findByFarmerId(farmerId).stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    public ProductDto updateProduct(Long productId, Long farmerId, ProductDto dto) {
        Product p = productRepository.findById(productId)
                .orElseThrow(NoSuchElementException::new);

        if (!farmerId.equals(p.getFarmerId())) {
            throw new SecurityException("Not owner");
        }

        // update fields
        if (dto.getName() != null) p.setName(dto.getName());
        if (dto.getCategory() != null) p.setCategory(dto.getCategory());
        if (dto.getUnit() != null) p.setUnit(dto.getUnit());
        if (dto.getPricePerUnit() != null) p.setPricePerUnit(dto.getPricePerUnit());
        if (dto.getQuantityAvailable() != null) p.setQuantityAvailable(dto.getQuantityAvailable());
        if (dto.getImageUrl() != null) p.setImageUrl(dto.getImageUrl());

        Product saved = productRepository.save(p);
        return toDto(saved);
    }

    public void deleteProduct(Long productId, Long farmerId) {
        Product p = productRepository.findById(productId)
                .orElseThrow(NoSuchElementException::new);
        if (!farmerId.equals(p.getFarmerId())) throw new SecurityException("Not owner");
        productRepository.deleteById(productId);
    }

    // helper
    private ProductDto toDto(Product p) {
        return new ProductDto(
                p.getId(),
                p.getFarmerId(),
                p.getName(),
                p.getCategory(),
                p.getUnit(),
                p.getPricePerUnit(),
                p.getQuantityAvailable(),
                p.getImageUrl()
        );
    }
}
