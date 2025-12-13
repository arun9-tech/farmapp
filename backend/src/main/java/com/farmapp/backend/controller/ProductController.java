package com.farmapp.backend.controller;

import com.farmapp.backend.dto.ProductDto;
import com.farmapp.backend.service.ProductService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/products")
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    // public listing
    @GetMapping
    public ResponseEntity<List<ProductDto>> listAll() {
        return ResponseEntity.ok(productService.listAll());
    }

    // public get
    @GetMapping("/{id}")
    public ResponseEntity<?> get(@PathVariable Long id) {
        ProductDto dto = productService.getById(id);
        if (dto == null) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(dto);
    }

    // create - authenticated
    @PostMapping
    public ResponseEntity<?> create(@RequestBody ProductDto dto, Principal principal) {
        if (principal == null) return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized");
        Long farmerId = parsePrincipalAsId(principal);
        if (farmerId == null) return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid token subject");
        ProductDto created = productService.createProduct(farmerId, dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    // my products - authenticated
    @GetMapping("/my")
    public ResponseEntity<?> myProducts(Principal principal) {
        if (principal == null) return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized");
        Long farmerId = parsePrincipalAsId(principal);
        if (farmerId == null) return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid token subject");
        return ResponseEntity.ok(productService.listByFarmer(farmerId));
    }

    // update - only owner
    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable Long id, @RequestBody ProductDto dto, Principal principal) {
        if (principal == null) return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized");
        Long farmerId = parsePrincipalAsId(principal);
        if (farmerId == null) return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid token subject");
        try {
            ProductDto updated = productService.updateProduct(id, farmerId, dto);
            return ResponseEntity.ok(updated);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        } catch (SecurityException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Forbidden");
        }
    }

    // delete - only owner
    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id, Principal principal) {
        if (principal == null) return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized");
        Long farmerId = parsePrincipalAsId(principal);
        if (farmerId == null) return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid token subject");
        try {
            productService.deleteProduct(id, farmerId);
            return ResponseEntity.noContent().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        } catch (SecurityException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Forbidden");
        }
    }

    private Long parsePrincipalAsId(Principal principal) {
        try {
            return Long.parseLong(principal.getName());
        } catch (Exception ex) {
            return null;
        }
    }
}
