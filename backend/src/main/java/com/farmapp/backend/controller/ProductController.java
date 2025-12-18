package com.farmapp.backend.controller;

import com.farmapp.backend.dto.ProductRequest;
import com.farmapp.backend.dto.ProductResponse;
import com.farmapp.backend.entity.Farmer;
import com.farmapp.backend.entity.Product;
import com.farmapp.backend.repository.FarmerRepository;
import com.farmapp.backend.repository.ProductRepository;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/farmer/products")
public class ProductController {

    private final ProductRepository productRepository;
    private final FarmerRepository farmerRepository;

    public ProductController(ProductRepository productRepository,
                             FarmerRepository farmerRepository) {
        this.productRepository = productRepository;
        this.farmerRepository = farmerRepository;
    }

    @PostMapping
    public ProductResponse addProduct(@RequestBody ProductRequest req) {

        Farmer farmer = farmerRepository.findById(req.farmerId)
                .orElseThrow(() -> new RuntimeException("Farmer not found"));

        Product p = new Product();
        p.setFarmer(farmer);
        p.setName(req.name);
        p.setCategory(req.category);
        p.setUnit(req.unit);
        p.setPricePerUnit(req.pricePerUnit);
        p.setQuantityAvailable(req.quantityAvailable);
        p.setImageUrl(req.imageUrl);

        return map(productRepository.save(p));
    }

    @GetMapping
    public List<ProductResponse> getAll() {
        return productRepository.findAll().stream()
                .map(this::map).toList();
    }

    private ProductResponse map(Product p) {
        ProductResponse r = new ProductResponse();
        r.id = p.getId();
        r.name = p.getName();
        r.category = p.getCategory();
        r.unit = p.getUnit();
        r.pricePerUnit = p.getPricePerUnit();
        r.quantityAvailable = p.getQuantityAvailable();
        r.imageUrl = p.getImageUrl();
        r.farmerId = p.getFarmer().getId();
        r.farmerName = p.getFarmer().getName();
        r.farmName = p.getFarmer().getFarmName();
        r.location = p.getFarmer().getLocation();
        return r;
    }
}

