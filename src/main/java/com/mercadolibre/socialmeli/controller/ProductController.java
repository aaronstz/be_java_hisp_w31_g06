package com.mercadolibre.socialmeli.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import com.mercadolibre.socialmeli.service.IProductService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@RestController
@RequestMapping("/products")
public class ProductController {

    @Autowired
    private IProductService productService;

    /*
     * US 006: Obtener un listado de las publicaciones realizadas por los vendedores
     * que un usuario sigue en las últimas dos semanas
     */
    @GetMapping("/followed/{userId}/list")
    public ResponseEntity<?> getRecentSellerPostsForUser(@PathVariable Integer userId) {
        return new ResponseEntity<>(productService.getRecentSellerPostsForUser(userId), HttpStatus.OK);
    }

}
