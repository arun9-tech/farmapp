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

    // ✅ ADD PRODUCT (Farmer)
    @PostMapping
    public ProductResponse addProduct(@RequestBody ProductRequest req) {

        Farmer farmer = farmerRepository.findById(req.farmerId)
                .orElseThrow(() -> new RuntimeException("Farmer not found"));

        Product product = new Product();
        product.setFarmer(farmer);
        product.setName(req.name);
        product.setCategory(req.category);
        product.setUnit(req.unit);
        product.setPricePerUnit(req.pricePerUnit);
        product.setQuantityAvailable(req.quantityAvailable);
        product.setImageUrl(req.imageUrl);

        Product saved = productRepository.save(product);

        return mapToResponse(saved);
    }

    @GetMapping
    public List<ProductResponse> getAllProducts() {
        return productRepository.findAll().stream().map(p -> {
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
        }).toList();
    }


    // 🔒 PRIVATE MAPPER (VERY IMPORTANT)
    private ProductResponse mapToResponse(Product p) {
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
