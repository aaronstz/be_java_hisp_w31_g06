package com.mercadolibre.socialmeli.repository;

import com.mercadolibre.socialmeli.entity.Post;
import com.mercadolibre.socialmeli.entity.Product;

import java.util.List;
import java.util.Set;

public interface IProductRepository {
    List<Product> findAll();

    void savePost(Post post);

    Set<Post> findRecentPostsForUser(Integer userId);

    Post findPostById(Integer postId);

}
