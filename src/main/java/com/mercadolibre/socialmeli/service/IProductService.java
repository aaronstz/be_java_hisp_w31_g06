package com.mercadolibre.socialmeli.service;

import com.mercadolibre.socialmeli.dto.FollowingPostDto;
import com.mercadolibre.socialmeli.dto.PostDto;

public interface IProductService {

    void createPost(PostDto post);
    FollowingPostDto getPost(Integer userId);
}
