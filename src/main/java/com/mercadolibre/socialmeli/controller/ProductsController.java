package com.mercadolibre.socialmeli.controller;

import com.mercadolibre.socialmeli.dto.ProductDto;
import com.mercadolibre.socialmeli.service.IProductService;
import com.mercadolibre.socialmeli.service.ProductServiceImpl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path="/products")
public class ProductsController {

    IProductService productService;

    public ProductsController(ProductServiceImpl productService) {
        this.productService = productService;
    }

    @GetMapping()
    public ResponseEntity<List<ProductDto>> getAllPosts() {
        return new ResponseEntity<>(productService.getAll(), HttpStatus.OK);
    }
}
