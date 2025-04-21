package com.mercadolibre.socialmeli.service;

import com.mercadolibre.socialmeli.dto.FollowingPostDto;
import com.mercadolibre.socialmeli.dto.PostDto;
import com.mercadolibre.socialmeli.dto.ProductDto;
import com.mercadolibre.socialmeli.dto.PromoPostCountDto;
import com.mercadolibre.socialmeli.entity.Post;

import java.util.List;

public interface IProductService {
    List<ProductDto> getAll();

    PostDto createPost(Post post);

    FollowingPostDto getRecentSellerPostsForUser(Integer userId, String order);

    PromoPostCountDto getPromoPostCount(Integer userId);

    FollowingPostDto getSellerPostsForUserByKeyword(Integer userId, String keyword);
}
