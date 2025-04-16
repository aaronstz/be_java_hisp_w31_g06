package com.mercadolibre.socialmeli.controller;

import com.mercadolibre.socialmeli.dto.PostDto;
import com.mercadolibre.socialmeli.dto.ProductDto;
import com.mercadolibre.socialmeli.entity.Post;
import com.mercadolibre.socialmeli.service.IProductService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "/products")

public class ProductsController {
    @Autowired
    IProductService productService;

    @GetMapping()
    public ResponseEntity<List<ProductDto>> getAllPosts() {
        return new ResponseEntity<>(productService.getAll(), HttpStatus.OK);
    }

    /*
     * US 006: Obtener un listado de las publicaciones realizadas por los vendedores
     * que un usuario sigue en las últimas dos semanas
     */
    @GetMapping("/followed/{userId}/list")
    public ResponseEntity<?> getRecentSellerPostsForUser(@PathVariable Integer userId) {
        return new ResponseEntity<>(productService.getRecentSellerPostsForUser(userId), HttpStatus.OK);
    }

    @PostMapping("/post")
    public ResponseEntity<PostDto> createPost(@RequestBody Post post) {
        return new ResponseEntity<>(productService.createPost(post), HttpStatus.OK);
    }
}
