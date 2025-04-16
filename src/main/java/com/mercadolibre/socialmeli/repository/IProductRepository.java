package com.mercadolibre.socialmeli.repository;

import com.mercadolibre.socialmeli.entity.Post;

import java.util.Set;

public interface IProductRepository {

    void savePost(Post post);

    Set<Post> findPost(Integer userId);

    Post findPostById(Integer postId);

}
