package com.mercadolibre.socialmeli.service;

import com.mercadolibre.socialmeli.dto.*;

import java.util.List;

public interface IProductService {
    List<ProductDto> getAll();
    PostDto createPost(CreatePostDto post);
    FollowingPostDto getRecentSellerPostsForUser(Integer userId, String order);
    PromoPostCountDto getPromoPostCount(Integer userId);
    FollowingPostDto getSellerPostsForUserByKeyword(Integer userId, String keyword);
    PromoPostDto getPromosBySeller(Integer userId);
    FollowingPostDto getSellerPostsForUserByCategory(Integer userId, Integer categoryId);
    List<PostDto> getAllPromos();
}
