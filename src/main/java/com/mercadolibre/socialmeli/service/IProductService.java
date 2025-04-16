package com.mercadolibre.socialmeli.service;

import com.mercadolibre.socialmeli.dto.FollowingPostDto;
import com.mercadolibre.socialmeli.dto.ProductDto;
import com.mercadolibre.socialmeli.entity.Post;

import java.util.List;

public interface IProductService {

    String createPost(Post post);

    FollowingPostDto getRecentSellerPostsForUser(Integer userId);

    List<ProductDto> getAll();

}
