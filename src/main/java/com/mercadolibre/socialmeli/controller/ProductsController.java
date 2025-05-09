package com.mercadolibre.socialmeli.controller;

import com.mercadolibre.socialmeli.dto.*;
import com.mercadolibre.socialmeli.service.IProductService;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "/products")
@Validated
public class ProductsController {
    @Autowired
    IProductService productService;

    @GetMapping()
    public ResponseEntity<List<ProductDto>> getAllPosts() {
        return new ResponseEntity<>(productService.getAll(), HttpStatus.OK);
    }

    @GetMapping("/followed/{userId}/list")
    public ResponseEntity<FollowingPostDto> getRecentSellerPostsForUser(@PathVariable  Integer userId,
                                                         @RequestParam(defaultValue = "date_desc") String order) {
        return new ResponseEntity<>(productService.getRecentSellerPostsForUser(userId, order), HttpStatus.OK);
    }

    @PostMapping("/post")
    public ResponseEntity<PostDto> createPost(@RequestBody @Valid CreatePostDto post) {
        return new ResponseEntity<>(productService.createPost(post), HttpStatus.OK);
    }

    @PostMapping("/promo-post")
    public ResponseEntity<PostDto> createPostWithDiscount(@RequestBody @Valid CreatePostDto post) {
        return new ResponseEntity<>(productService.createPost(post), HttpStatus.OK);
    }

    @GetMapping("/promo-post/count")
    public ResponseEntity<?> getPromoPostCount(@RequestParam int user_id) {
        return new ResponseEntity<>(productService.getPromoPostCount(user_id), HttpStatus.OK);
    }

    @GetMapping("/promo-post/list")
    public ResponseEntity<PromoPostDto> getPromosBySeller(@RequestParam Integer userId) {
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

    @GetMapping("/followed/{userId}/filterByCategory")
    public ResponseEntity<FollowingPostDto> getSellerPostsByCategory(@PathVariable Integer userId,
                                                                     @RequestParam Integer filterCategory) {
        return new ResponseEntity<>(productService.getSellerPostsForUserByCategory(userId, filterCategory), HttpStatus.OK);
    }
}
