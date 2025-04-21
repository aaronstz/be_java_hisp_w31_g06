package com.mercadolibre.socialmeli.controller;

import com.mercadolibre.socialmeli.dto.FollowingPostDto;
import com.mercadolibre.socialmeli.dto.PostDto;
import com.mercadolibre.socialmeli.dto.ProductDto;
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

    @GetMapping("/followed/{userId}/list")
    public ResponseEntity<?> getRecentSellerPostsForUser(@PathVariable Integer userId,
            @RequestParam(defaultValue = "date_desc") String order) {

        if (!order.equals("date_asc") && !order.equals("date_desc")) {
            return new ResponseEntity<>("El orden solo puede ser 'date_asc' o 'date_desc'", HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(productService.getRecentSellerPostsForUser(userId, order), HttpStatus.OK);
    }

    @PostMapping("/post")
    public ResponseEntity<PostDto> createPost(@RequestBody PostDto post) {
        return new ResponseEntity<>(productService.createPost(post), HttpStatus.OK);
    }

    @PostMapping("/promo-post")
    public ResponseEntity<PostDto> createPostWithDiscount(@RequestBody PostDto post) {
        return new ResponseEntity<>(productService.createPost(post), HttpStatus.OK);
    }

    @GetMapping("/promo-post/count")
    public ResponseEntity<?> getPromoPostCount(@RequestParam int user_id) {
        return new ResponseEntity<>(productService.getPromoPostCount(user_id), HttpStatus.OK);
    }

    @GetMapping("/promo-post/list")
    public ResponseEntity<List<PostDto>> getPromosBySeller(@RequestParam Integer userId) {
        return new ResponseEntity<>(productService.getPromosBySeller(userId), HttpStatus.OK);
    }

    @GetMapping("/promotions")
    public ResponseEntity<List<PostDto>> getAllPromos() {
        return new ResponseEntity<>(productService.getAllPromos(), HttpStatus.OK);
    }

    @GetMapping("/followed/{userId}/filterByKeyword")
    public ResponseEntity<FollowingPostDto> getSellerPostsForUserByKeyword(@PathVariable int userId,
            @RequestParam String keyword) {
        return new ResponseEntity<>(productService.getSellerPostsForUserByKeyword(userId, keyword),
                HttpStatus.OK);
    }
}
